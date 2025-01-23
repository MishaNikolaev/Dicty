package com.nmichail.dicty.domain.usecases

import com.nmichail.dicty.domain.model.Word
import com.nmichail.dicty.domain.repository.DictionaryRepository
import javax.inject.Inject

class GetWordDefinitionUseCase @Inject constructor(
    private val repository: DictionaryRepository
) {
    suspend operator fun invoke(word: String): List<Word> {
        return repository.getWordDefinition(word)
    }
}