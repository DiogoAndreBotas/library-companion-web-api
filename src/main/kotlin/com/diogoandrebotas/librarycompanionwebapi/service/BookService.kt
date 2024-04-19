package com.diogoandrebotas.librarycompanionwebapi.service

import com.diogoandrebotas.librarycompanionwebapi.exception.BookNotFoundException
import com.diogoandrebotas.librarycompanionwebapi.exception.GoogleBooksApiException
import com.diogoandrebotas.librarycompanionwebapi.model.Author
import com.diogoandrebotas.librarycompanionwebapi.model.Book
import com.diogoandrebotas.librarycompanionwebapi.repository.AuthorRepository
import com.diogoandrebotas.librarycompanionwebapi.repository.BookRepository
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrElse

@Service
class BookService(
    private val googleBooksService: GoogleBooksService,
    private val bookRepository: BookRepository,
    private val authorRepository: AuthorRepository,
) {
    fun getBooks(): List<Book> = bookRepository.findAll()

    fun getBook(isbn: String): Book {
        return bookRepository.findById(isbn).getOrElse {
            throw BookNotFoundException("Book with ISBN $isbn not found")
        }
    }

    fun addBook(isbn: String): Optional<Book> {
        val databaseBook = bookRepository.findById(isbn)
        if (databaseBook.isPresent) {
            return databaseBook
        }

        val books = googleBooksService.getBookWithIsbn(isbn)

        if (books.items.isEmpty()) {
            throw GoogleBooksApiException("Book with ISBN $isbn not found in Google Books API")
        }

        val bookResponse = books.items.first().volumeInfo

        val authors = bookResponse.authors.map {
            authorRepository.findById(it).orElse(
                authorRepository.save(
                    Author(name = it)
                )
            )
        }.toMutableList()

        return Optional.of(
            bookRepository.save(
                Book(
                    title = bookResponse.title,
                    authors = authors,
                    pages = bookResponse.pageCount,
                    isbn = isbn,
                    publishDate = bookResponse.publishedDate,
                    imageUrl = bookResponse.imageLinks.thumbnail
                )
            )
        )
    }
}