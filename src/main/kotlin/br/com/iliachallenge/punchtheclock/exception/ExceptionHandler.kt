package br.com.iliachallenge.punchtheclock.exception

import br.com.iliachallenge.punchtheclock.dto.ErrorResponseDTO
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@RestControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequest::class)
    fun badRequestException(exception: BadRequest) = ErrorResponseDTO(exception.msg)

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(Forbidden::class)
    fun forbiddenException(exception: Forbidden) = ErrorResponseDTO(exception.msg)

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFound::class)
    fun notFoundException(exception: NotFound) = ErrorResponseDTO(exception.msg)

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(Conflict::class)
    fun conflictException(exception: Conflict) = ErrorResponseDTO(exception.msg)

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception::class)
    fun unexpectedException(exception: Exception) = ErrorResponseDTO("Whoops! Something went wrong.")
}