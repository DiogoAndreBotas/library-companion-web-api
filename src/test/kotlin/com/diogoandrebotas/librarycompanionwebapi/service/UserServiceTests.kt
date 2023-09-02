package com.diogoandrebotas.librarycompanionwebapi.service

import com.diogoandrebotas.librarycompanionwebapi.exception.GoogleBooksApiException
import com.diogoandrebotas.librarycompanionwebapi.exception.UserNotFoundException
import com.diogoandrebotas.librarycompanionwebapi.model.AppUser
import com.diogoandrebotas.librarycompanionwebapi.model.Book
import com.diogoandrebotas.librarycompanionwebapi.model.IsbnInput
import com.diogoandrebotas.librarycompanionwebapi.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import java.util.*

@ExtendWith(MockitoExtension::class)
class UserServiceTests {

    @Mock
    private lateinit var bookService: BookService

    @Test
    fun getBooks_ShouldReturnEmptyList_WhenUserHasNoBooks() {
        // Given
        val userRepository = mock<UserRepository> {
            on { findById("username") } doReturn Optional.of(AppUser("username"))
        }
        val userService = UserService(userRepository, bookService)

        // When
        val books = userService.getBooks("username")

        // Then
        assertTrue(books.isEmpty())
    }

    @Test
    fun getBooks_ShouldReturnBookList_WhenUserHasBooks() {
        // Given
        val books = mutableSetOf(
            Book("9780593099322", "Dune", listOf("Frank Herbert"), 658, "2019-10"),
            Book("9780593098233", "Dune Messiah", listOf("Frank Herbert"), 337, "2019-06"),
            Book("9780593098240", "Children of Dune", listOf("Frank Herbert"), 609, "2019-06"),
            Book("9780593098257", "God Emperor of Dune", listOf("Frank Herbert"), 587, "2019-06"),
            Book("9780593098264", "Heretics of Dune", listOf("Frank Herbert"), 669, "2019-06"),
            Book("9780593098271", "ChapterHouse: Dune", listOf("Frank Herbert"), 624, "2019-06")
        )
        val userRepository = mock<UserRepository> {
            on { findById("username") } doReturn Optional.of(AppUser("username", books))
        }
        val userService = UserService(userRepository, bookService)

        // When
        val userBooks = userService.getBooks("username")

        // Then
        assertEquals(books, userBooks)
    }

    @Test
    fun getBooks_ShouldThrowAnException_WhenUserIsNotFound() {
        // Given
        val userRepository = mock<UserRepository> {
            on { findById("username") } doReturn Optional.empty()
        }
        val userService = UserService(userRepository, bookService)

        // When + Then
        assertThrows<UserNotFoundException> {
            userService.getBooks("username")
        }
    }

    @Test
    fun addBook_ShouldAddTheBook_WhenItIsFoundEitherInDatabaseOrGoogleBooks() {
        // Given
        val book = Book("9780593099322", "Dune", listOf("Frank Herbert"), 658, "2019-10")
        val bookService = mock<BookService> {
            on { addBookWithIsbn(any<IsbnInput>()) } doReturn Optional.of(book)
        }
        val userRepository = mock<UserRepository> {
            on { findById("username") } doReturn Optional.of(AppUser("username"))
            on { save(any<AppUser>()) } doReturn AppUser("username", mutableSetOf(book))
        }
        val userService = UserService(userRepository, bookService)

        // When
        val user = userService.addBook("username", "9780593099322")

        // Then
        assertEquals(setOf(book), user.books)
    }

    @Test
    fun addBook_ShouldThrowAnException_WhenUserIsNotFound() {
        val userRepository = mock<UserRepository> {
            on { findById("username") } doReturn Optional.empty()
        }
        val userService = UserService(userRepository, bookService)

        assertThrows<UserNotFoundException> {
            userService.addBook("username", "9780593099322")
        }
    }

    @Test
    fun addBook_ShouldThrowAnException_WhenTheBookIsNotFoundInGoogleBooksAPI() {
        // Given
        val bookService = mock<BookService> {
            on { addBookWithIsbn(any<IsbnInput>()) } doReturn Optional.empty()
        }
        val userRepository = mock<UserRepository> {
            on { findById("username") } doReturn Optional.of(AppUser("username"))
        }
        val userService = UserService(userRepository, bookService)

        // When + Then
        assertThrows<GoogleBooksApiException> {
            userService.addBook("username", "9780593099322")
        }
    }

}