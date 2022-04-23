package com.example.rickandmortyapp.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class CharacterInfo(
    @Json(name = "count") val count: Int,
    @Json(name = "next") val next: String,
    @Json(name = "pages") val pages: Int,
    @Json(name = "prev") val prev: Int?
) : Parcelable