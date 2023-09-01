package com.diogoandrebotas.librarycompanionwebapi.controller

import com.diogoandrebotas.librarycompanionwebapi.model.AppUser
import com.diogoandrebotas.librarycompanionwebapi.model.IsbnInput
import com.diogoandrebotas.librarycompanionwebapi.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api")
class UserController(
    private val userService: UserService
) {
    @GetMapping("/users/{username}/books")
    fun getBooksFromUser(@PathVariable username: String) = ResponseEntity.ok(userService.getBooks(username))

    @PostMapping("/users/{username}/books")
    fun addBookToUser(@PathVariable username: String, @RequestBody isbnInput: IsbnInput): ResponseEntity<AppUser> {
        return ResponseEntity
            .created(URI("/books/${isbnInput.isbn}"))
            .body(userService.addBook(username, isbnInput.isbn))
    }
}