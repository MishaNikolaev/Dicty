package com.nmichail.dicty.domain.model

data class Word(
    val word: String,
    val phonetic: String?,
    val origin: String?,
    val meanings: List<Meaning>
)

