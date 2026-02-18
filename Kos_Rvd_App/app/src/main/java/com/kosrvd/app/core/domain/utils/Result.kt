package com.kosrvd.app.core.domain.utils

typealias DomainError = Error

sealed interface Result<out D, out E: Error> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error <out E: DomainError>(val error: E) : Result<Nothing, E>
}

// Extension functions untuk Result
inline fun <T, E: Error, R> Result<T, E>.map(transform: (T) -> R): Result<R, E> {
    return when(this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(transform(data))
    }
}

inline fun <T, E: Error> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    if (this is Result.Success) action(data)
    return this
}

inline fun <T, E: Error> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    if (this is Result.Error) action(error)
    return this
}

// GetOrNull versi improved
fun <T, E: Error> Result<T, E>.getOrNull(): T? = when (this) {
    is Result.Success -> data
    is Result.Error -> null
}

// GetOrElse untuk default value
inline fun <T, E: Error> Result<T, E>.getOrElse(defaultValue: (E) -> T): T = when (this) {
    is Result.Success -> data
    is Result.Error -> defaultValue(error)
}

// FlatMap untuk chaining operations
inline fun <T, E: Error, R> Result<T, E>.flatMap(transform: (T) -> Result<R, E>): Result<R, E> {
    return when(this) {
        is Result.Success -> transform(data)
        is Result.Error -> Result.Error(error)
    }
}