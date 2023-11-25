package com.diogoandrebotas.librarycompanionwebapi.exception

class BookAlreadyExistsException(override val message: String) : RuntimeException(message)