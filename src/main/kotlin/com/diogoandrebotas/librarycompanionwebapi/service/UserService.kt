package com.diogoandrebotas.librarycompanionwebapi.service

import com.diogoandrebotas.librarycompanionwebapi.exception.GoogleBooksApiException
import com.diogoandrebotas.librarycompanionwebapi.exception.UserNotFoundException
import com.diogoandrebotas.librarycompanionwebapi.model.AppUser
import com.diogoandrebotas.librarycompanionwebapi.model.Book
import com.diogoandrebotas.librarycompanionwebapi.model.IsbnInput
import com.diogoandrebotas.librarycompanionwebapi.repository.UserRepository
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrElse

@Service
class UserService(
    private val userRepository: UserRepository,
    private val bookService: BookService
) {
    fun getBooks(username: String): MutableSet<Book> {
        return userRepository.findById(username)
            .getOrElse { throw UserNotFoundException(username) }
            .books
    }

    fun addBook(username: String, isbn: String): AppUser {
        val user = userRepository.findById(username)
            .getOrElse { throw UserNotFoundException(username) }

        val bookToAdd = bookService.addBookWithIsbn(IsbnInput(isbn)).getOrElse {
            throw GoogleBooksApiException("Book with ISBN $isbn not found")
        }

        user.books.add(bookToAdd)

        return userRepository.save(user)
    }
}