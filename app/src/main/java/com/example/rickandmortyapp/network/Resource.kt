package com.example.rickandmortyapp.network

data class Resource<out T>(
    val status: Status,
    val data: T? = null,
    val message: String?,
    val errorCode: Int? = null
) {
    companion object {

        fun <T> success(data: T? = null): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String? = null, errorCode: Int? = null): Resource<T> {
            return Resource(Status.ERROR, null, msg, errorCode)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}