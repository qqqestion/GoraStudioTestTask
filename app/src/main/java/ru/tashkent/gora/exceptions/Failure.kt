package ru.tashkent.gora.exceptions

/**
 * Base class for errors/exceptions.
 */
sealed class Failure {
    object ServerError : Failure()
    object NetworkError : Failure()
    object TimeoutError : Failure()
}
