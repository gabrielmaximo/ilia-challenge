package br.com.iliachallenge.punchtheclock.exception

sealed class AppError(cause: Throwable?): RuntimeException(cause)

data class BadRequest(val msg: String, override val cause: Throwable? = null) : AppError(cause)

data class Forbidden(val msg: String, override val cause: Throwable? = null) : AppError(cause)

data class Conflict(val msg: String, override val cause: Throwable? = null) : AppError(cause)

data class NotFound(val msg: String, override val cause: Throwable? = null) : AppError(cause)


