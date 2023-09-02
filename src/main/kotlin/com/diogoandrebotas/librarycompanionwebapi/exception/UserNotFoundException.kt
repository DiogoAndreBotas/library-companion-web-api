package com.diogoandrebotas.librarycompanionwebapi.exception

class UserNotFoundException(username: String) : Exception("User with username $username not found.")