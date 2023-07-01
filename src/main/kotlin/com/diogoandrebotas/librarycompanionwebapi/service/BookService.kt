package com.diogoandrebotas.librarycompanionwebapi.service

import com.diogoandrebotas.librarycompanionwebapi.model.Book
import com.diogoandrebotas.librarycompanionwebapi.model.BookInput
import com.diogoandrebotas.librarycompanionwebapi.repository.BookRepository
import org.springframework.stereotype.Service

@Service
class BookService(
    private val googleBooksService: GoogleBooksService,
    private val bookRepository: BookRepository
) {

    fun getBooks(): List<Book> = bookRepository.findAll()

    fun getBookById(id: Long) = bookRepository.findById(id)

    fun addBookWithIsbn(bookInput: BookInput): List<Book> {
        val isbn = bookInput.isbn

        if (bookRepository.findByIsbn(isbn).isPresent)
            throw Exception("Book already exists in database")

        return googleBooksService.getBookWithIsbn(isbn).items.map {
            bookRepository.save(
                Book(
                    title = it.volumeInfo.title,
                    author = it.volumeInfo.authors.joinToString(separator = ", "),
                    pages = it.volumeInfo.pageCount,
                    isbn = isbn,
                    imageUrl = it.volumeInfo.imageLinks.thumbnail
                )
            )
        }
    }

    fun updateBook(id: Long, updatedBook: Book): Book {
        updatedBook.id = id
        return bookRepository.save(updatedBook)
    }

    fun deleteBook(id: Long) = bookRepository.deleteById(id)

}