package com.nmichail.dicty.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nmichail.dicty.presentation.viewmodel.DictionaryEvent
import com.nmichail.dicty.presentation.viewmodel.DictionaryViewModel

@Composable
fun DictionaryScreen(viewModel: DictionaryViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        var inputWord by remember { mutableStateOf("") }

        TextField(
            value = inputWord,
            onValueChange = { inputWord = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter a word") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { viewModel.onEvent(DictionaryEvent.GetWord(inputWord)) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        state.word?.let { words ->
            words.forEach { word ->
                Text(
                    text = "Word: ${word.word}"
                )
                Text("Phonetic: ${word.phonetic ?: "N/A"}")
                Text("Origin: ${word.origin ?: "N/A"}")

                word.meanings.forEach { meaning ->
                    Text(
                        text = "${meaning.partOfSpeech}:")
                    meaning.definitions.forEach { definition ->
                        Text("- ${definition.definition}")
                        definition.example?.let {
                            Text("(e.g., $it)", textAlign = TextAlign.Start)
                        }
                    }
                }
            }
        }

        state.error?.let { error ->
            Text(
                text = "Error: $error",
                color = Color(0xFFFF0000),
                textAlign = TextAlign.Center
            )
        }
    }
}