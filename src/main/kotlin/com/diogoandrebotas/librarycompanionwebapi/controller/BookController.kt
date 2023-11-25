package com.diogoandrebotas.librarycompanionwebapi.controller

import com.diogoandrebotas.librarycompanionwebapi.model.Book
import com.diogoandrebotas.librarycompanionwebapi.model.IsbnInput
import com.diogoandrebotas.librarycompanionwebapi.service.BookService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class BookController(
    private val bookService: BookService
) {
    @GetMapping("/books")
    fun getBooks() = bookService.getBooks()

    @GetMapping("/books/{isbn}")
    fun getBookById(@PathVariable isbn: String): ResponseEntity<Book> {
        val book = bookService.getBook(isbn)
        return ResponseEntity.ok(book.get())
    }

    @PostMapping("/books")
    fun addBookWithIsbn(@RequestBody isbnInput: IsbnInput): ResponseEntity<Book> {
        val book = bookService.addBookWithIsbn(isbnInput)
        return ResponseEntity.ok(book.get())
    }

    @DeleteMapping("/books/{isbn}")
    fun deleteBook(@PathVariable isbn: String): ResponseEntity<Void> {
        bookService.deleteBook(isbn)
        return ResponseEntity.noContent().build()
    }
}