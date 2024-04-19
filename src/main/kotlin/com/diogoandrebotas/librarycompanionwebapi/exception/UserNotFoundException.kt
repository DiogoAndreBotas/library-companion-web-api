package com.diogoandrebotas.librarycompanionwebapi.exception

class UserNotFoundException(override val message: String) : RuntimeException(message)