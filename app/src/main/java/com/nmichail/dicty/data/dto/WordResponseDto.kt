package com.nmichail.dicty.data.dto
import com.nmichail.dicty.domain.model.Meaning

data class WordResponseDto(
    val word: String,
    val phonetic: String?,
    val phonetics: List<PhoneticDto>,
    val origin: String?,
    val meanings: List<Meaning>
)