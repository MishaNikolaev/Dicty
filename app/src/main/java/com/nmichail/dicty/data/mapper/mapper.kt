package com.nmichail.dicty.data.mapper

import com.nmichail.dicty.data.dto.WordResponseDto
import com.nmichail.dicty.domain.model.Word

fun WordResponseDto.toWord(): Word {
    return Word(
        word = this.word,
        phonetic = this.phonetic,
        origin = this.origin,
        meanings = this.meanings
    )
}