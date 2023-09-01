package com.diogoandrebotas.librarycompanionwebapi.controller

import com.diogoandrebotas.librarycompanionwebapi.model.Book
import com.diogoandrebotas.librarycompanionwebapi.model.IsbnInput
import com.diogoandrebotas.librarycompanionwebapi.service.BookService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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

        return if (book.isEmpty)
            ResponseEntity.notFound().build()
        else
            ResponseEntity.ok(book.get())
    }

    @PostMapping("/books")
    fun addBookWithIsbn(@RequestBody isbnInput: IsbnInput): ResponseEntity<Book> {
        val book = bookService.addBookWithIsbn(isbnInput)

        return if (book.isEmpty)
            ResponseEntity.unprocessableEntity().build()
        else
            ResponseEntity.ok(book.get())
    }

    @PutMapping("/books/{isbn}")
    fun updateBook(@PathVariable isbn: String, @RequestBody book: Book) = bookService.updateBook(isbn, book)

    @DeleteMapping("/books/{isbn}")
    fun deleteBook(@PathVariable isbn: String): ResponseEntity<Void> {
        bookService.deleteBook(isbn)
        return ResponseEntity.noContent().build()
    }
}