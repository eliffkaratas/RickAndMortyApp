package com.example.rickandmortyapp.network.repository

import com.example.rickandmortyapp.model.CharacterResponse
import com.example.rickandmortyapp.network.Resource
import com.example.rickandmortyapp.network.service.CharacterService
import java.io.IOException
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val characterService: CharacterService
) {
    suspend fun getCharacters(page: Int): Resource<CharacterResponse> {
        return try {
            val body = characterService.getCharacters(page)
            Resource.success(body)
        } catch (e: IOException) {
            Resource.error("Network error")
        } catch (e: retrofit2.HttpException) {
            Resource.error("HTTP ${e.code()}")
        } catch (e: Exception) {
            Resource.error("Unexpected error")
        }
    }
}