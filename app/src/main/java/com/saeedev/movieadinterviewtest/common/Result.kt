package com.saeedev.movieadinterviewtest.common

sealed class Result<out T> {

    object Loading : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val error: Throwable, var showDialog: Boolean = false) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$error]"
            Loading -> "Loading"
        }
    }
}

inline fun <R> Result<R>.onSuccess(action: (R) -> Unit): Result<R> {
    if (this is Result.Success) {
        action(data)
    }
    return this
}