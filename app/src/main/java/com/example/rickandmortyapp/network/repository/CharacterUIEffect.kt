package com.example.rickandmortyapp.network.repository

sealed interface CharacterUiEffect {
    data class ShowSnackbar(val message: String) : CharacterUiEffect
}