package com.diogoandrebotas.librarycompanionwebapi.service

import com.diogoandrebotas.librarycompanionwebapi.exception.BookNotFoundException
import com.diogoandrebotas.librarycompanionwebapi.exception.UserContainsBookException
import com.diogoandrebotas.librarycompanionwebapi.exception.UserNotFoundException
import com.diogoandrebotas.librarycompanionwebapi.model.AppUser
import com.diogoandrebotas.librarycompanionwebapi.model.Book
import com.diogoandrebotas.librarycompanionwebapi.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.Optional
import kotlin.jvm.optionals.getOrElse

@Service
class UserService(
    private val userRepository: UserRepository,
    private val bookService: BookService
) {
    fun addUser(username: String): Optional<AppUser> {
        val user = AppUser(username = username)
        return Optional.of(userRepository.save(user))
    }

    fun getUser(userId: String): AppUser {
        return userRepository.findById(userId.toLong()).getOrElse {
            throw UserNotFoundException("User with ID $userId does not exist")
        }
    }

    fun deleteUser(userId: String): Unit = userRepository.deleteById(userId.toLong())

    fun getBooks(userId: String): Optional<List<Book>> {
        return userRepository.findById(userId.toLong())
            .map { it.books }
    }

    fun getBook(userId: String, isbn: String): Book {
        val user = userRepository.findById(userId.toLong()).getOrElse {
            throw UserNotFoundException("User with ID $userId does not exist")
        }

        return user.books.find { it.isbn == isbn }
            ?: throw BookNotFoundException("User with ID $userId does not contain book with ISBN $isbn")
    }

    fun addBook(userId: String, isbn: String): AppUser {
        val user = getUser(userId)

        if (user.books.map { it.isbn }.contains(isbn)) {
            throw UserContainsBookException("User with ID $userId already contains book with ISBN $isbn")
        }

        bookService.addBook(isbn).get().also {
            user.books.add(it)
        }

        return userRepository.save(user)
    }

    fun deleteBook(userId: String, isbn: String) {
        val user = userRepository.findById(userId.toLong()).get()
        user.books.removeIf { it.isbn == isbn }
        userRepository.save(user)
    }
}