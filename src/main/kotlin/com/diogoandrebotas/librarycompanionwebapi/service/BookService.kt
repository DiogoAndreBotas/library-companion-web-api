package com.diogoandrebotas.librarycompanionwebapi.service

import com.diogoandrebotas.librarycompanionwebapi.model.Book
import com.diogoandrebotas.librarycompanionwebapi.model.BookInput
import com.diogoandrebotas.librarycompanionwebapi.repository.BookRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class BookService(
    private val googleBooksService: GoogleBooksService,
    private val bookRepository: BookRepository
) {
    fun getBooks(): List<Book> = bookRepository.findAll()

    fun getBookById(id: Long) = bookRepository.findById(id)

    fun addBookWithIsbn(bookInput: BookInput): Optional<Book> {
        val isbn = bookInput.isbn

        // TODO: Uncomment when books are detached from user in DB
        // TODO: Need better way of communicating what the error was
        // Optional is not great here (need global exception handler :) )
        if (bookRepository.findByIsbn(isbn).isPresent) {
            return Optional.empty()
        }

        // Assume there is only one book with the given ISBN
        val books = googleBooksService.getBookWithIsbn(isbn)

        if (books.items.isEmpty()) {
            return Optional.empty()
        }

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

    fun updateBook(id: Long, updatedBook: Book): Book {
        updatedBook.id = id
        return bookRepository.save(updatedBook)
    }

    fun deleteBook(id: Long) = bookRepository.deleteById(id)
}