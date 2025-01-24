package com.nmichail.dicty.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nmichail.dicty.presentation.viewmodel.DictionaryEvent
import com.nmichail.dicty.presentation.viewmodel.DictionaryViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.nmichail.dicty.presentation.notification.NotificationService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictionaryScreen(viewModel: DictionaryViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    var inputWord by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        TextField(
            value = inputWord,
            onValueChange = { inputWord = it },
            textStyle = TextStyle.Default.copy(fontSize = 24.sp),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            placeholder = { Text("Enter a word") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                viewModel.onEvent(DictionaryEvent.GetWord(inputWord))
            }),
            trailingIcon = {
                IconButton(onClick = {
                    viewModel.onEvent(DictionaryEvent.GetWord(inputWord))
                }) {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = "Search Icon")
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = Color(0xFF007CB4),
                unfocusedIndicatorColor = Color.LightGray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Загрузка
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }

        state.word?.let { words ->
            words.forEach { word ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Word: ${word.word}",
                        fontSize = 20.sp,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = "Phonetic: ${word.phonetic ?: "N/A"}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    word.phonetics.forEach { phonetic ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Phonetics: ${phonetic.text ?: "N/A"}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.weight(1f)
                            )
                            phonetic.audio?.let { audioUrl ->
                                Button(
                                    onClick = { playAudio(context, audioUrl) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007CB4))
                                ) {
                                    Text("Pronunciation")
                                }
                            }
                        }
                    }

                    word.meanings.forEach { meaning ->
                        Text(
                            text = "Part of Speech: ${meaning.partOfSpeech}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        meaning.definitions.forEachIndexed { index, definition ->
                            Text(text = "${index + 1}. ${definition.definition}")
                            definition.example?.let {
                                Text(
                                    text = "Example: $it",
                                    color = Color.Gray
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        state.error?.let { error ->
            Text(
                text = "Error: That's word ain't in the dictionary or you need to access the Internet $error",
                color = Color.Red,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { startNotificationService(context) },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007CB4))
        ) {
            Text(text = "Show Notification")
        }
    }
}

private fun playAudio(context: Context, audioUrl: String) {
    val player = ExoPlayer.Builder(context).build().apply {
        val mediaItem = MediaItem.fromUri(audioUrl)
        setMediaItem(mediaItem)
        prepare()
        playWhenReady = true
    }

    player.addListener(object : Player.Listener {
        override fun onPlaybackStateChanged(state: Int) {
            if (state == Player.STATE_ENDED) {
                player.release()
            }
        }
    })
}

fun startNotificationService(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val serviceIntent = Intent(context, NotificationService::class.java)
        context.startForegroundService(serviceIntent)
    } else {
        val serviceIntent = Intent(context, NotificationService::class.java)
        context.startService(serviceIntent)
    }
}