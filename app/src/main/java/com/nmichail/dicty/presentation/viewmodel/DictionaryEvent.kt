package com.nmichail.dicty.presentation.viewmodel

sealed class DictionaryEvent {
    data class GetWord(val word: String) : DictionaryEvent()
}