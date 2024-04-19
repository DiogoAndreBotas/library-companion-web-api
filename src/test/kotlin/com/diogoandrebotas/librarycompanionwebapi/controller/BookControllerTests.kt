package com.diogoandrebotas.librarycompanionwebapi.controller

import com.diogoandrebotas.librarycompanionwebapi.exception.BookNotFoundException
import com.diogoandrebotas.librarycompanionwebapi.exception.GoogleBooksApiException
import com.diogoandrebotas.librarycompanionwebapi.model.Author
import com.diogoandrebotas.librarycompanionwebapi.model.Book
import com.diogoandrebotas.librarycompanionwebapi.model.IsbnInput
import com.diogoandrebotas.librarycompanionwebapi.service.BookService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.springframework.http.HttpStatus
import java.util.*

@ExtendWith(MockitoExtension::class)
class BookControllerTests {
    @Test
    fun `getBooks returns a list of Books`() {
        val expectedBooks = listOf(
            Book(
                title = "Dune",
                authors = mutableListOf(Author(name = "Frank Herbert")),
                pages = 658,
                isbn = "9780593099322",
                publishDate = "2019-10",
                imageUrl = "https://books.google.com/books/dune_url"
            ),
            Book(
                title = "Dune Messiah",
                authors = mutableListOf(Author(name = "Frank Herbert")),
                pages = 337,
                isbn = "9780593098233",
                publishDate = "2019-10",
                imageUrl = "https://books.google.com/books/dune_messiah_url"
            ),
            Book(
                title = "Children of Dune",
                authors = mutableListOf(Author(name = "Frank Herbert")),
                pages = 408,
                isbn = "9780593098240",
                publishDate = "2019-10",
                imageUrl = "https://books.google.com/books/children_of_dune_url"
            )
        )
        val bookService = mock<BookService> {
            on { getBooks() } doReturn expectedBooks
        }
        val bookController = BookController(bookService)

        val actualBooks = bookController.getBooks()

        assertEquals(expectedBooks, actualBooks)
    }

    @Test
    fun `getBooks returns an empty list of Books`() {
        val expectedBooks = emptyList<Book>()
        val bookService = mock<BookService> {
            on { getBooks() } doReturn expectedBooks
        }
        val bookController = BookController(bookService)

        val actualBooks = bookController.getBooks()

        assertEquals(expectedBooks, actualBooks)
    }

    @Test
    fun `getBookById returns 200 OK with a Book as the response body`() {
        val expectedBook = Book(
            title = "Dune",
            authors = mutableListOf(Author(name = "Frank Herbert")),
            pages = 658,
            isbn = "9780593099322",
            publishDate = "2019-10",
            imageUrl = "https://books.google.com/books/dune_url"
        )
        val bookService = mock<BookService> {
            on { getBook("9780593099322") } doReturn expectedBook
        }
        val bookController = BookController(bookService)

        val response = bookController.getBook("9780593099322")
        val actualBook = response.body

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(expectedBook, actualBook)
    }

    @Test
    fun `getBookById throws an exception when the Book is not found`() {
        val bookService = mock<BookService> {
            on { getBook("9780593099322") } doThrow BookNotFoundException("")
        }
        val bookController = BookController(bookService)

        assertThrows<BookNotFoundException> { bookController.getBook("9780593099322") }
    }

    @Test
    fun `addBookWithIsbn returns 200 OK when the Book is created`() {
        val expectedBook = Book(
            title = "Dune",
            authors = mutableListOf(Author(name = "Frank Herbert")),
            pages = 658,
            isbn = "9780593099322",
            publishDate = "2019-10",
            imageUrl = "https://books.google.com/books/dune_url"
        )
        val isbnInput = IsbnInput("9780593099322")
        val bookService = mock<BookService> {
            on { addBook("9780593099322") } doReturn Optional.of(expectedBook)
        }
        val bookController = BookController(bookService)

        val response = bookController.addBook(isbnInput)
        val actualBook = response.body

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(expectedBook, actualBook)
    }

    @Test
    fun `addBookWithIsbn throws an exception when there is an error related with Google Books API`() {
        val isbnInput = IsbnInput("9780593099322")
        val bookService = mock<BookService> {
            on { addBook("9780593099322") } doThrow GoogleBooksApiException("")
        }
        val bookController = BookController(bookService)

        assertThrows<GoogleBooksApiException> { bookController.addBook(isbnInput) }
    }
}