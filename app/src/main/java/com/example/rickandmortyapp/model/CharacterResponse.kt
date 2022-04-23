package com.example.rickandmortyapp.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class CharacterResponse(
    @Json(name = "info") val info: CharacterInfo,
    @Json(name = "results") val results: List<CharacterResult>
) : Parcelable