package com.diogoandrebotas.librarycompanionwebapi.service

import com.diogoandrebotas.librarycompanionwebapi.model.Book
import com.diogoandrebotas.librarycompanionwebapi.model.BookInput
import com.diogoandrebotas.librarycompanionwebapi.repository.BookRepository
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class BookService(
    private val openLibraryService: OpenLibraryService,
    private val bookRepository: BookRepository
) {

    fun getBooks(): List<Book> = bookRepository.findAll()

    fun getBookById(id: Long) = bookRepository.findById(id)

    fun addBookWithIsbn(bookInput: BookInput): Book {
        val isbn = bookInput.isbn

        if (bookRepository.findByIsbn(isbn).isPresent)
            throw Exception("Book already exists in database")

        val bookResponse = openLibraryService.getBookWithIsbn(isbn)

        val authors = runBlocking {
            openLibraryService.getAuthorsAndUploadCoverToS3(bookResponse, isbn)
        }

        return bookRepository.save(
            Book(
                title = bookResponse.title,
                authors = authors,
                pages = bookResponse.numberOfPages,
                isbn = isbn,
                publishDate = bookResponse.publishDate
            )
        )
    }

    fun updateBook(id: Long, updatedBook: Book): Book {
        updatedBook.id = id
        return bookRepository.save(updatedBook)
    }

    fun deleteBook(id: Long) = bookRepository.deleteById(id)
}