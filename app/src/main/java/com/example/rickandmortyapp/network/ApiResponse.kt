package com.example.rickandmortyapp.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response

sealed class ApiResponse<T> {

    companion object {

        private val moshi: Moshi by lazy {
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        }

        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            return ApiErrorResponse(
                errorCode = -1,
                errorMessage = error.message ?: "unknown error"
            )
        }

        fun <T> create(response: Response<T>): ApiResponse<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    ApiSuccessResponse(body)
                } else {
                    ApiErrorResponse(
                        errorCode = response.code(),
                        errorMessage = "Empty response body"
                    )
                }
            } else {
                val fallbackMsg = response.message().takeIf { it.isNotBlank() } ?: "unknown error"

                val errorMsg = try {
                    val errorBodyStr = response.errorBody()?.string()
                    if (errorBodyStr.isNullOrBlank()) {
                        fallbackMsg
                    } else {
                        val adapter = moshi.adapter(ApiResponseError::class.java)
                        val parsed = adapter.fromJson(errorBodyStr)
                        parsed?.error?.takeIf { it.isNotBlank() } ?: fallbackMsg
                    }
                } catch (e: Exception) {
                    e.message ?: fallbackMsg
                }

                ApiErrorResponse(
                    errorCode = response.code(),
                    errorMessage = errorMsg
                )
            }
        }
    }
}

data class ApiSuccessResponse<T>(val body: T) : ApiResponse<T>()

data class ApiErrorResponse<T>(
    val errorCode: Int,
    val errorMessage: String?
) : ApiResponse<T>()