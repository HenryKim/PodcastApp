package com.example.coadingchallenge.data.api

sealed class ApiResult<out T> {
    data class Success<T>(val data: T, val meta: Meta = Meta()) : ApiResult<T>()
    data class Error(
        val code: Int,
        val message: String,
        val throwable: Throwable? = null
    ) : ApiResult<Nothing>()
}

data class Meta(val size: Int = 0, val page: Int = 0, val totalPages: Int = 0)