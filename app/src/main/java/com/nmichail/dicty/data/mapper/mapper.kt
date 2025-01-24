package com.nmichail.dicty.data.mapper

import com.nmichail.dicty.data.dto.PhoneticDto
import com.nmichail.dicty.data.dto.WordResponseDto
import com.nmichail.dicty.domain.model.Phonetic
import com.nmichail.dicty.domain.model.Word

fun WordResponseDto.toWord(): Word {
    return Word(
        word = this.word,
        phonetic = this.phonetic,
        origin = this.origin,
        meanings = this.meanings,
        phonetics = this.phonetics.map { it.toPhonetic() }
    )
}

fun PhoneticDto.toPhonetic(): Phonetic {
    return Phonetic(
        text = this.text,
        audio = this.audio
    )
}