package com.bandits.badmintonmanager.util

sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String, val exception: Throwable? = null) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}

fun <T> Resource<T>.getDataOrNull(): T? = when (this) {
    is Resource.Success -> data
    else -> null
}

fun <T> Resource<T>.isSuccess(): Boolean = this is Resource.Success

fun <T> Resource<T>.isError(): Boolean = this is Resource.Error

fun <T> Resource<T>.isLoading(): Boolean = this is Resource.Loading
