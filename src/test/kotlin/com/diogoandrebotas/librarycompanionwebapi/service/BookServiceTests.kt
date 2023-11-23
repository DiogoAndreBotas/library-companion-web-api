package com.diogoandrebotas.librarycompanionwebapi.service

import com.diogoandrebotas.librarycompanionwebapi.exception.BookAlreadyExistsException
import com.diogoandrebotas.librarycompanionwebapi.exception.GoogleBooksApiException
import com.diogoandrebotas.librarycompanionwebapi.model.*
import com.diogoandrebotas.librarycompanionwebapi.repository.BookRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import java.util.*

@ExtendWith(MockitoExtension::class)
class BookServiceTests {

    @Test
    fun `getBooks returns a list of Books when they exist in the database`() {
        val expectedBooks = listOf(
            Book(
                title = "Dune",
                authors = listOf("Frank Herbert"),
                pages = 658,
                isbn = "9780593099322",
                publishDate = "2019-10",
                imageUrl = "https://books.google.com/books/dune_url"
            ),
            Book(
                title = "Dune Messiah",
                authors = listOf("Frank Herbert"),
                pages = 337,
                isbn = "9780593098233",
                publishDate = "2019-10",
                imageUrl = "https://books.google.com/books/dune_messiah_url"
            ),
            Book(
                title = "Children of Dune",
                authors = listOf("Frank Herbert"),
                pages = 408,
                isbn = "9780593098240",
                publishDate = "2019-10",
                imageUrl = "https://books.google.com/books/children_of_dune_url"
            )
        )
        val bookRepository = mock<BookRepository> {
            on { findAll() } doReturn expectedBooks
        }
        val bookService = BookService(mock<GoogleBooksService>(), bookRepository)

        val actualBooks = bookService.getBooks()

        assertEquals(expectedBooks, actualBooks)
    }

    @Test
    fun `getBooks returns an empty list of Books when none exist in the database`() {
        val expectedBooks = emptyList<Book>()
        val bookRepository = mock<BookRepository> {
            on { findAll() } doReturn expectedBooks
        }
        val bookService = BookService(mock<GoogleBooksService>(), bookRepository)

        val actualBooks = bookService.getBooks()

        assertEquals(expectedBooks, actualBooks)
    }

    @Test
    fun `getBook returns a Book when it exists in the database`() {
        val expectedBook = Book(
            title = "Dune",
            authors = listOf("Frank Herbert"),
            pages = 658,
            isbn = "9780593099322",
            publishDate = "2019-10",
            imageUrl = "https://books.google.com/books/dune_url"
        )
        val bookRepository = mock<BookRepository> {
            on { findById("9780593099322") } doReturn Optional.of(expectedBook)
        }
        val bookService = BookService(mock<GoogleBooksService>(), bookRepository)

        val actualBook = bookService.getBook("9780593099322").get()

        assertEquals(expectedBook, actualBook)
    }

    @Test
    fun `getBook returns an empty Optional when the Book does not exist in the database`() {
        val bookRepository = mock<BookRepository> {
            on { findById("9780593099322") } doReturn Optional.empty()
        }
        val bookService = BookService(mock<GoogleBooksService>(), bookRepository)

        val actualBook = bookService.getBook("9780593099322")

        assertEquals(Optional.empty<Book>(), actualBook)
    }

    @Test
    fun `addBookWithIsbn saves the Book to the database when it exists in Google Books API`() {
        val expectedBook = Book(
            title = "Dune",
            authors = listOf("Frank Herbert"),
            pages = 658,
            isbn = "9780593099322",
            publishDate = "2019-10",
            imageUrl = "https://books.google.com/books/dune_url"
        )
        val googleBooksResponse = GoogleBooksResponse(
            totalItems = 1,
            items = listOf(
                GoogleBooksItem(
                    GoogleBooksVolumeInfo(
                        title = "Dune",
                        authors = listOf("Frank Herbert"),
                        pageCount = 658,
                        publishedDate = "2019-10",
                        imageLinks = GoogleBooksImageLinks("https://books.google.com/books/dune_url")
                    )
                )
            )
        )
        val bookRepository = mock<BookRepository> {
            on { findById("9780593099322") } doReturn Optional.empty()
            on { save(any<Book>()) } doReturn expectedBook
        }
        val googleBooksService = mock<GoogleBooksService> {
            on { getBookWithIsbn("9780593099322") } doReturn googleBooksResponse
        }
        val bookService = BookService(googleBooksService, bookRepository)

        val actualBook = bookService.addBookWithIsbn(IsbnInput("9780593099322")).get()

        assertEquals(expectedBook, actualBook)
    }

    @Test
    fun `addBookWithIsbn throws BookAlreadyExistsException when it exists in the database`() {
        val book = Book(
            title = "Dune",
            authors = listOf("Frank Herbert"),
            pages = 658,
            isbn = "9780593099322",
            publishDate = "2019-10",
            imageUrl = "https://books.google.com/books/dune_url"
        )
        val bookRepository = mock<BookRepository> {
            on { findById("9780593099322") } doReturn Optional.of(book)
        }
        val bookService = BookService(mock<GoogleBooksService>(), bookRepository)

        assertThrows<BookAlreadyExistsException> { bookService.addBookWithIsbn(IsbnInput("9780593099322")) }
    }

    @Test
    fun `addBookWithIsbn throws GoogleBooksApiException when it is not found in Google Books API`() {
        val googleBooksResponse = GoogleBooksResponse(
            totalItems = 0,
            items = emptyList()
        )
        val bookRepository = mock<BookRepository> {
            on { findById("9780593099322") } doReturn Optional.empty()
        }
        val googleBooksService = mock<GoogleBooksService> {
            on { getBookWithIsbn("9780593099322") } doReturn googleBooksResponse
        }
        val bookService = BookService(googleBooksService, bookRepository)

        assertThrows<GoogleBooksApiException> { bookService.addBookWithIsbn(IsbnInput("9780593099322")) }
    }

}