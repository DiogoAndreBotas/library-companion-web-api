package com.diogoandrebotas.librarycompanionwebapi.controller

import com.diogoandrebotas.librarycompanionwebapi.model.Book
import com.diogoandrebotas.librarycompanionwebapi.model.IsbnInput
import com.diogoandrebotas.librarycompanionwebapi.service.BookService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/books")
class BookController(
    private val bookService: BookService
) {
    @GetMapping
    fun getBooks(): ResponseEntity<List<Book>> {
        val books = bookService.getBooks()
        return ResponseEntity.ok(books)
    }

    @GetMapping("/{isbn}")
    fun getBook(@PathVariable isbn: String): ResponseEntity<Book> {
        val book = bookService.getBook(isbn)
        return ResponseEntity.ok(book)
    }

    @PostMapping
    fun addBook(@RequestBody isbnInput: IsbnInput): ResponseEntity<Book> {
        val book = bookService.addBook(isbnInput.isbn)
        return ResponseEntity.ok(book.get())
    }
}