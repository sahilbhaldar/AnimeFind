package com.example.animefind.domain.util

sealed class Resource<out T> {
    class Idle<out T> : Resource<T>()
    class Loading<out T> : Resource<T>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error<out T>(val message: String) : Resource<T>()
}
