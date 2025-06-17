package com.example.tumbuhnyata.data.util

/**
 * A generic class that holds a value with its loading status.
 * Used to wrap network responses and provide unified error handling.
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
} 