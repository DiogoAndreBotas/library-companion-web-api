package com.diogoandrebotas.librarycompanionwebapi.service

import com.diogoandrebotas.librarycompanionwebapi.exception.BookAlreadyExistsException
import com.diogoandrebotas.librarycompanionwebapi.exception.BookNotFoundException
import com.diogoandrebotas.librarycompanionwebapi.exception.GoogleBooksApiException
import com.diogoandrebotas.librarycompanionwebapi.model.*
import com.diogoandrebotas.librarycompanionwebapi.repository.AuthorRepository
import com.diogoandrebotas.librarycompanionwebapi.repository.BookRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import java.util.*

@ExtendWith(MockitoExtension::class)
class BookServiceTests {

    @Test
    fun `getBooks returns a list of Books when they exist in the database`() {
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
        val bookRepository = mock<BookRepository> {
            on { findAll() } doReturn expectedBooks
        }
        val authorRepository = mock<AuthorRepository>()
        val bookService = BookService(mock<GoogleBooksService>(), bookRepository, authorRepository)

        val actualBooks = bookService.getBooks()

        assertEquals(expectedBooks, actualBooks)
    }

    @Test
    fun `getBooks returns an empty list of Books when none exist in the database`() {
        val expectedBooks = emptyList<Book>()
        val bookRepository = mock<BookRepository> {
            on { findAll() } doReturn expectedBooks
        }
        val authorRepository = mock<AuthorRepository>()
        val bookService = BookService(mock<GoogleBooksService>(), bookRepository, authorRepository)

        val actualBooks = bookService.getBooks()

        assertEquals(expectedBooks, actualBooks)
    }

    @Test
    fun `getBook returns a Book when it exists in the database`() {
        val expectedBook = Book(
            title = "Dune",
            authors = mutableListOf(Author(name = "Frank Herbert")),
            pages = 658,
            isbn = "9780593099322",
            publishDate = "2019-10",
            imageUrl = "https://books.google.com/books/dune_url"
        )
        val bookRepository = mock<BookRepository> {
            on { findById("9780593099322") } doReturn Optional.of(expectedBook)
        }
        val authorRepository = mock<AuthorRepository>()
        val bookService = BookService(mock<GoogleBooksService>(), bookRepository, authorRepository)

        val actualBook = bookService.getBook("9780593099322")

        assertEquals(expectedBook, actualBook)
    }

    @Test
    fun `getBook throws an exception when the Book does not exist in the database`() {
        val bookRepository = mock<BookRepository> {
            on { findById("9780593099322") } doThrow BookNotFoundException("")
        }
        val authorRepository = mock<AuthorRepository>()
        val bookService = BookService(mock<GoogleBooksService>(), bookRepository, authorRepository)

        assertThrows<BookNotFoundException> { bookService.getBook("9780593099322") }
    }

    @Test
    fun `addBook saves the Book to the database when it exists in Google Books API`() {
        val expectedBook = Book(
            title = "Dune",
            authors = mutableListOf(Author(name = "Frank Herbert")),
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
        val googleBooksService = mock<GoogleBooksService> {
            on { getBookWithIsbn("9780593099322") } doReturn googleBooksResponse
        }
        val bookRepository = mock<BookRepository> {
            on { findById("9780593099322") } doReturn Optional.empty()
            on { save(any<Book>()) } doReturn expectedBook
        }
        val authorRepository = mock<AuthorRepository> {
            on { findById("Frank Herbert") } doReturn Optional.empty()
            on { save(any()) } doReturn Author(name = "Frank Herbert")
        }
        val bookService = BookService(googleBooksService, bookRepository, authorRepository)

        val actualBook = bookService.addBook("9780593099322").get()

        assertEquals(expectedBook, actualBook)
    }

    @Test
    fun `addBook throws BookAlreadyExistsException when it exists in the database`() {
        val book = Book(
            title = "Dune",
            authors = mutableListOf(Author(name = "Frank Herbert")),
            pages = 658,
            isbn = "9780593099322",
            publishDate = "2019-10",
            imageUrl = "https://books.google.com/books/dune_url"
        )
        val bookRepository = mock<BookRepository> {
            on { findById("9780593099322") } doReturn Optional.of(book)
        }
        val authorRepository = mock<AuthorRepository>()
        val bookService = BookService(mock<GoogleBooksService>(), bookRepository, authorRepository)

        assertThrows<BookAlreadyExistsException> { bookService.addBook("9780593099322") }
    }

    @Test
    fun `addBook throws GoogleBooksApiException when it is not found in Google Books API`() {
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
        val authorRepository = mock<AuthorRepository>()
        val bookService = BookService(googleBooksService, bookRepository, authorRepository)

        assertThrows<GoogleBooksApiException> { bookService.addBook("9780593099322") }
    }

}