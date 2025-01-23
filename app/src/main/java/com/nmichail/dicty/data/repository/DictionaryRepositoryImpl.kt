package com.nmichail.dicty.data.repository

import com.nmichail.dicty.data.mapper.toWord
import com.nmichail.dicty.data.remote.DictionaryApi
import com.nmichail.dicty.domain.model.Word
import com.nmichail.dicty.domain.repository.DictionaryRepository
import javax.inject.Inject

class DictionaryRepositoryImpl @Inject constructor(
    private val api: DictionaryApi
) : DictionaryRepository {
    override suspend fun getWordDefinition(word: String): List<Word> {
        return api.getWordDefinition(word).map { it.toWord() }
    }
}