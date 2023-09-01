package com.diogoandrebotas.librarycompanionwebapi.service

import com.diogoandrebotas.librarycompanionwebapi.model.AppUser
import com.diogoandrebotas.librarycompanionwebapi.model.IsbnInput
import com.diogoandrebotas.librarycompanionwebapi.repository.UserRepository
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrElse

@Service
class UserService(
    private val userRepository: UserRepository,
    private val bookService: BookService
) {
    fun getBooks(username: String) = userRepository.findById(username).get().books

    fun addBook(username: String, isbn: String): AppUser {
        val user = userRepository.findById(username).get()
        val bookToAdd = bookService.getBook(isbn).getOrElse { bookService.addBookWithIsbn(IsbnInput(isbn)).get() }

        user.books.add(bookToAdd)

        return userRepository.save(user)
    }
}