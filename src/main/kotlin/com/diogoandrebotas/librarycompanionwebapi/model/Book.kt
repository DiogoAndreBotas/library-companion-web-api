package com.diogoandrebotas.librarycompanionwebapi.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "books")
class Book(
    @Id
    val isbn: String = "",
    val title: String = "",
    val authors: List<String> = emptyList(),
    val pages: Int = -1,
    val publishDate: String = "",
    val imageUrl: String = ""
)