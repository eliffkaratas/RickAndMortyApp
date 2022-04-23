package com.example.rickandmortyapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.rickandmortyapp.model.CharacterResponse
import com.example.rickandmortyapp.network.Resource
import com.example.rickandmortyapp.network.repository.CharacterRepository
import com.example.rickandmortyapp.network.repository.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel@Inject constructor(val repository: CharacterRepository) : ViewModel() {

    private var listCharacters = MutableLiveData<Event<Unit>>()

    fun characterResponse(page: Int): LiveData<Event<Resource<CharacterResponse>>> =
        Transformations
            .switchMap(listCharacters) {
                repository.getCharacters(page)
            }

    fun getCharacters() {
        listCharacters.value = Event(Unit)
    }
}