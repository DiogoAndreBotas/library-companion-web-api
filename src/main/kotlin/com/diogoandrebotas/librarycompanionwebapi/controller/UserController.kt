package com.diogoandrebotas.librarycompanionwebapi.controller

import com.diogoandrebotas.librarycompanionwebapi.model.AppUser
import com.diogoandrebotas.librarycompanionwebapi.model.Book
import com.diogoandrebotas.librarycompanionwebapi.model.IsbnInput
import com.diogoandrebotas.librarycompanionwebapi.model.UserInput
import com.diogoandrebotas.librarycompanionwebapi.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    val userService: UserService
) {
    @PostMapping
    fun addUser(@RequestBody userInput: UserInput): ResponseEntity<AppUser> {
        return ResponseEntity.ok(userService.addUser(userInput.username).get())
    }

    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: String): ResponseEntity<AppUser> {
        return ResponseEntity.ok(userService.getUser(userId))
    }

    @DeleteMapping("/{userId}")
    fun deleteUser(@PathVariable userId: String): ResponseEntity<Void> {
        userService.deleteUser(userId)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{userId}/books")
    fun getBooks(@PathVariable userId: String): ResponseEntity<List<Book>> {
        val books = userService.getBooks(userId)
        return ResponseEntity.ok(books.get())
    }

    @GetMapping("/{userId}/books/{isbn}")
    fun getBook(@PathVariable userId: String, @PathVariable isbn: String): ResponseEntity<Book> {
        return ResponseEntity.ok(userService.getBook(userId, isbn))
    }

    @PostMapping("/{userId}/books")
    fun addBook(@PathVariable userId: String, @RequestBody isbnInput: IsbnInput): ResponseEntity<AppUser> {
        return ResponseEntity.ok(userService.addBook(userId, isbnInput.isbn))
    }

    @DeleteMapping("/{userId}/books/{isbn}")
    fun deleteBook(@PathVariable userId: String, @PathVariable isbn: String): ResponseEntity<Void> {
        userService.deleteBook(userId, isbn)
        return ResponseEntity.noContent().build()
    }
}