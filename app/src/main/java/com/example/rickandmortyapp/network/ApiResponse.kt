package com.example.rickandmortyapp.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response

sealed class ApiResponse<T> {
    companion object {
        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            return ApiErrorResponse(errorMessage = error.message ?: "unknown error")
        }

        fun <T> create(response: Response<T>): ApiResponse<T> {
            return if (response.isSuccessful) {
                ApiSuccessResponse(response.body()!!)
            } else {
                try {
                    val moshi = Moshi.Builder()
                        .add(KotlinJsonAdapterFactory())
                        .build()
                    val jsonAdapter = moshi.adapter(ApiResponseError::class.java)
                    val baseValidation = jsonAdapter.fromJson(response.errorBody()!!.source())!!
                    val errorMsg = if (baseValidation.error.message.isNullOrEmpty()) {
                        response.message()
                    } else {
                        baseValidation.error.message
                    }
                    ApiErrorResponse(errorMessage = errorMsg ?: "unknown error")
                } catch (e: Exception) {
                    ApiErrorResponse(errorMessage = e.message ?: "unknown error")
                }

            }
        }
    }
}

data class ApiSuccessResponse<T>(val body: T) : ApiResponse<T>()

data class ApiErrorResponse<T>(val errorCode: Int = 1000, val errorMessage: String?) :
    ApiResponse<T>()