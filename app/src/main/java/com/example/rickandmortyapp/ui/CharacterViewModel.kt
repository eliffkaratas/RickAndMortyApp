package com.example.rickandmortyapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortyapp.network.Status
import com.example.rickandmortyapp.network.repository.CharacterRepository
import com.example.rickandmortyapp.network.repository.CharacterUiEffect
import com.example.rickandmortyapp.network.repository.CharacterUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val repository: CharacterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CharacterUiState>(CharacterUiState.Idle)
    val uiState: StateFlow<CharacterUiState> = _uiState

    private val _uiEffect = MutableSharedFlow<CharacterUiEffect>()
    val uiEffect: SharedFlow<CharacterUiEffect> = _uiEffect

    private var currentPage = 1

    fun loadPage(page: Int) {
        currentPage = page

        viewModelScope.launch {
            _uiState.value = CharacterUiState.Loading

            val result = repository.getCharacters(page)
            when (result.status) {
                Status.SUCCESS -> {
                    val data = result.data
                    if (data != null) {
                        _uiState.value = CharacterUiState.Success(data)
                    } else {
                        _uiState.value = CharacterUiState.Error("Empty body")
                        _uiEffect.emit(CharacterUiEffect.ShowSnackbar("Empty body"))
                    }
                }
                Status.ERROR -> {
                    val msg = result.message ?: "Unknown error"
                    _uiState.value = CharacterUiState.Error(msg)
                    _uiEffect.emit(CharacterUiEffect.ShowSnackbar(msg))
                }
                Status.LOADING -> Unit // repository zaten loading dönmüyor genelde
            }
        }
    }

    fun refresh() = loadPage(currentPage)
}