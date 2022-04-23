package com.example.rickandmortyapp.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponseError(
    @Json(name = "error")
    val error: Error
)