package com.diogoandrebotas.librarycompanionwebapi.controller

import com.diogoandrebotas.librarycompanionwebapi.model.Book
import com.diogoandrebotas.librarycompanionwebapi.model.BookInput
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

    @GetMapping("/books/{id}")
    fun getBookById(@PathVariable id: Long): ResponseEntity<Book> {
        val book = bookService.getBookById(id)

        return if (book.isEmpty)
            ResponseEntity.notFound().build()
        else
            ResponseEntity.ok(book.get())
    }

    @PostMapping("/books")
    fun addBookWithIsbn(@RequestBody bookInput: BookInput): ResponseEntity<Book> {
        val book = bookService.addBookWithIsbn(bookInput)

        return if (book.isEmpty)
            ResponseEntity.unprocessableEntity().build()
        else
            ResponseEntity.ok(book.get())
    }

    @PutMapping("/books/{id}")
    fun updateBook(@PathVariable id: Long, @RequestBody book: Book) = bookService.updateBook(id, book)

    @DeleteMapping("/books/{id}")
    fun deleteBook(@PathVariable id: Long): ResponseEntity<Void> {
        bookService.deleteBook(id)
        return ResponseEntity.noContent().build()
    }
}