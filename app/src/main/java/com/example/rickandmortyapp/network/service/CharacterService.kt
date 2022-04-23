package com.example.rickandmortyapp.network.service

import com.example.rickandmortyapp.model.CharacterResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CharacterService {
    @GET("api/character")
    fun getCharacters(@Query("page") page: Int): Call<CharacterResponse>
}