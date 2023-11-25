package com.diogoandrebotas.librarycompanionwebapi.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BookAlreadyExistsException::class)
    fun handleBookAlreadyExistsException(): ResponseEntity<Unit> = ResponseEntity.status(HttpStatus.CONFLICT).build()

    @ExceptionHandler(BookNotFoundException::class)
    fun handleBookNotFoundException(): ResponseEntity<Unit> = ResponseEntity.notFound().build()

    @ExceptionHandler(GoogleBooksApiException::class)
    fun handleGoogleBooksApiException(): ResponseEntity<Unit> = ResponseEntity.unprocessableEntity().build()

}