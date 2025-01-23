package com.nmichail.dicty.domain.repository

import com.nmichail.dicty.domain.model.Word

interface DictionaryRepository {
    suspend fun getWordDefinition(word: String): List<Word>
}