package com.nmichail.dicty.data.remote

import com.nmichail.dicty.data.dto.WordResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApi {
    @GET("api/v2/entries/en/{word}")
    suspend fun getWordDefinition(@Path("word") word: String): List<WordResponseDto>
}
