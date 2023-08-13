package com.diogoandrebotas.librarycompanionwebapi.repository

import com.diogoandrebotas.librarycompanionwebapi.model.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BookRepository : JpaRepository<Book, Long> {
    fun findByIsbn(isbn: String): Optional<Book>
}