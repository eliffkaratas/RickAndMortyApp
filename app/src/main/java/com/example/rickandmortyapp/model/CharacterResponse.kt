package com.example.rickandmortyapp.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class CharacterResponse(
    @Json(name = "info") val info: CharacterInfo,
    @Json(name = "results") val results: MutableList<CharacterResult>
) : Parcelable{
    @Parcelize
    @JsonClass(generateAdapter = true)
    data class CharacterInfo(
        @Json(name = "count") val count: Int,
        @Json(name = "next") val next: String,
        @Json(name = "pages") val pages: Int,
        @Json(name = "prev") val prev: Int?
    ) : Parcelable
    @Parcelize
    @JsonClass(generateAdapter = true)
    data class CharacterResult(
        @Json(name = "created") val created: String?,
        @Json(name = "episode") val episode: List<String>?,
        @Json(name = "gender") val gender: String?,
        @Json(name = "id") val id: Int?,
        @Json(name = "image") val image: String?,
        @Json(name = "characterLocation") val characterLocation: CharacterLocation?,
        @Json(name = "name") val name: String?,
        @Json(name = "characterOrigin") val characterOrigin: CharacterOrigin?,
        @Json(name = "species") val species: String?,
        @Json(name = "status") val status: String?,
        @Json(name = "type") val type: String?,
        @Json(name = "url") val url: String?
    ) : Parcelable {
        @Parcelize
        @JsonClass(generateAdapter = true)
        data class CharacterOrigin(
            @Json(name = "name") val name: String,
            @Json(name = "url") val url: String
        ) : Parcelable
        @Parcelize
        @JsonClass(generateAdapter = true)
        data class CharacterLocation(
            @Json(name = "name") val name: String,
            @Json(name = "url") val url: String
        ) : Parcelable
    }
}