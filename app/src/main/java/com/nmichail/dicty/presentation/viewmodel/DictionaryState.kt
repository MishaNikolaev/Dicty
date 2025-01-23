package com.nmichail.dicty.presentation.viewmodel

import com.nmichail.dicty.domain.model.Word

data class DictionaryState(
    val isLoading: Boolean = false,
    val word: List<Word>? = null,
    val error: String? = null
)