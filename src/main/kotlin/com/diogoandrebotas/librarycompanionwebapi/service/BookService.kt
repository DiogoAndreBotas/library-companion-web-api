package com.diogoandrebotas.librarycompanionwebapi.service

import com.diogoandrebotas.librarycompanionwebapi.model.Book
import com.diogoandrebotas.librarycompanionwebapi.repository.BookRepository
import org.springframework.stereotype.Service

@Service
class BookService(
    private val bookRepository: BookRepository
) {

    fun getBooks(): List<Book> = bookRepository.findAll()

    fun getBookById(id: Long) = bookRepository.findById(id)

    fun addBook(book: Book) = bookRepository.save(book)

    fun updateBook(id: Long, updatedBook: Book): Book {
        updatedBook.id = id
        return bookRepository.save(updatedBook)
    }

    fun deleteBook(id: Long) = bookRepository.deleteById(id)

}