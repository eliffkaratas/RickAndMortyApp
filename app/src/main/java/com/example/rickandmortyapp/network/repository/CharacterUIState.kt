package com.example.rickandmortyapp.network.repository

import com.example.rickandmortyapp.model.CharacterResponse

sealed interface CharacterUiState {
    data object Idle : CharacterUiState
    data object Loading : CharacterUiState
    data class Success(val data: CharacterResponse) : CharacterUiState
    data class Error(val message: String) : CharacterUiState
}