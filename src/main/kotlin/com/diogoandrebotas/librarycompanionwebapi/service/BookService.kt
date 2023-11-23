package com.diogoandrebotas.librarycompanionwebapi.service

import com.diogoandrebotas.librarycompanionwebapi.exception.BookAlreadyExistsException
import com.diogoandrebotas.librarycompanionwebapi.exception.GoogleBooksApiException
import com.diogoandrebotas.librarycompanionwebapi.model.Book
import com.diogoandrebotas.librarycompanionwebapi.model.IsbnInput
import com.diogoandrebotas.librarycompanionwebapi.repository.BookRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class BookService(
    private val googleBooksService: GoogleBooksService,
    private val bookRepository: BookRepository
) {
    fun getBooks(): List<Book> = bookRepository.findAll()

    fun getBook(isbn: String): Optional<Book> = bookRepository.findById(isbn)

    fun addBookWithIsbn(isbnInput: IsbnInput): Optional<Book> {
        val isbn = isbnInput.isbn

        if (bookRepository.findById(isbn).isPresent)
            throw BookAlreadyExistsException("Book with ISBN $isbn already exists")

        val books = googleBooksService.getBookWithIsbn(isbn)

        if (books.items.isEmpty())
            throw GoogleBooksApiException("Book with ISBN $isbn not found in Google Books API")

        val bookResponse = books.items.first().volumeInfo

        return Optional.of(
            bookRepository.save(
                Book(
                    title = bookResponse.title,
                    authors = bookResponse.authors,
                    pages = bookResponse.pageCount,
                    isbn = isbn,
                    publishDate = bookResponse.publishedDate,
                    imageUrl = bookResponse.imageLinks.thumbnail
                )
            )
        )
    }

    fun deleteBook(isbn: String): Unit = bookRepository.deleteById(isbn)
}