package com.example.rickandmortyapp.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class CharacterOrigin(
    @Json(name = "name") val name: String,
    @Json(name = "url") val url: String
) : Parcelable