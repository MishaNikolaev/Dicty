package com.nmichail.dicty.domain.model

data class Meaning(
    val partOfSpeech: String,
    val definitions: List<Definition>
)