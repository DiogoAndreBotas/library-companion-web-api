package com.diogoandrebotas.librarycompanionwebapi.service

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

    fun getBook(isbn: String) = bookRepository.findById(isbn)

    fun addBookWithIsbn(isbnInput: IsbnInput): Optional<Book> {
        val isbn = isbnInput.isbn
        val book = bookRepository.findById(isbn)

        if (book.isPresent)
            return book

        val books = googleBooksService.getBookWithIsbn(isbn)

        if (books.items.isEmpty())
            return Optional.empty()

        // Assume there is only one book with the given ISBN
        val bookResponse = books.items.first().volumeInfo

        googleBooksService.uploadCoverToS3(isbn, bookResponse.imageLinks.thumbnail)

        return Optional.of(
            bookRepository.save(
                Book(
                    title = bookResponse.title,
                    authors = bookResponse.authors,
                    pages = bookResponse.pageCount,
                    isbn = isbn,
                    publishDate = bookResponse.publishedDate
                )
            )
        )
    }

    fun updateBook(isbn: String, updatedBook: Book): Book {
        return bookRepository.save(updatedBook)
    }

    fun deleteBook(isbn: String) = bookRepository.deleteById(isbn)
}