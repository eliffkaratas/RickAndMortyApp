package com.example.rickandmortyapp.network.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.rickandmortyapp.AppExecutor
import com.example.rickandmortyapp.model.CharacterResponse
import com.example.rickandmortyapp.network.ApiErrorResponse
import com.example.rickandmortyapp.network.ApiResponse
import com.example.rickandmortyapp.network.ApiSuccessResponse
import com.example.rickandmortyapp.network.Resource
import com.example.rickandmortyapp.network.service.CharacterService
import java.io.IOException
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val appExecutor: AppExecutor,
    private val characterService: CharacterService
) {

    private val characterResult = MediatorLiveData<Event<Resource<CharacterResponse>>>()

    fun getCharacters(page: Int): LiveData<Event<Resource<CharacterResponse>>> {
        appExecutor.networkIO().execute {
            try {
                characterResult.postValue(Event(Resource.loading()))
                val response =
                    characterService.getCharacters(page).execute()
                when (val apiResponse = ApiResponse.create(response)) {
                    is ApiSuccessResponse -> {
                        characterResult.postValue(
                            Event(
                                Resource.success(
                                    response.body()
                                )
                            )
                        )
                    }
                    is ApiErrorResponse -> {
                        characterResult.postValue(Event(Resource.error(apiResponse.errorMessage)))
                    }
                }
            } catch (exception: IOException) {
            }
        }
        return characterResult
    }
}