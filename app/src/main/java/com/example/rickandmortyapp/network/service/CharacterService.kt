package com.example.rickandmortyapp.network.service

import com.example.rickandmortyapp.model.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CharacterService {
    @GET("api/character")
    suspend fun getCharacters(
        @Query("page") page: Int = 1
    ): CharacterResponse
}