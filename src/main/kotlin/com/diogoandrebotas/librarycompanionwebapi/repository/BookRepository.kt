package com.diogoandrebotas.librarycompanionwebapi.repository

import com.diogoandrebotas.librarycompanionwebapi.model.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : JpaRepository<Book, String>