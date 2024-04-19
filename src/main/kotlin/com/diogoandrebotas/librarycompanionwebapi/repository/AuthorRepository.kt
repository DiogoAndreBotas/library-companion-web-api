package com.diogoandrebotas.librarycompanionwebapi.repository

import com.diogoandrebotas.librarycompanionwebapi.model.Author
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthorRepository : JpaRepository<Author, String>