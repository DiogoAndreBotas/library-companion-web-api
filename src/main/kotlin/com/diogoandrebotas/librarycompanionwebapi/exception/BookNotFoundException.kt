package com.diogoandrebotas.librarycompanionwebapi.exception

class BookNotFoundException(override val message: String) : RuntimeException(message)