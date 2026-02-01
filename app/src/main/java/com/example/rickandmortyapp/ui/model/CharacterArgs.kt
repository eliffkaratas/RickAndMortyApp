package com.example.rickandmortyapp.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CharacterArgs(
    val characterImage: String? = null,
    val characterName: String? = null,
    val characterStatus: String? = null,
    val characterLocationName: String? = null,
    val characterLocationUrl: String? = null,
    val characterSpecies: String? = null,
    val characterGender: String? = null,
    val characterCreated: String? = null
) : Parcelable