package com.nmichail.dicty.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nmichail.dicty.domain.usecases.GetWordDefinitionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DictionaryViewModel @Inject constructor(
    private val getWordDefinitionUseCase: GetWordDefinitionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DictionaryState())
    val state: StateFlow<DictionaryState> = _state

    fun onEvent(event: DictionaryEvent) {
        when (event) {
            is DictionaryEvent.GetWord -> {
                getWord(event.word)
            }
        }
    }

    private fun getWord(word: String) {
        viewModelScope.launch {
            _state.value = DictionaryState(isLoading = true)
            try {
                val result = getWordDefinitionUseCase(word)
                _state.value = DictionaryState(word = result)
            } catch (e: Exception) {
                _state.value = DictionaryState(error = e.message)
            }
        }
    }
}