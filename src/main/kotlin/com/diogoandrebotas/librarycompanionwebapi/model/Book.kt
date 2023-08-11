package com.diogoandrebotas.librarycompanionwebapi.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = -1,
    val title: String = "",
    val authors: List<String> = emptyList(),
    val pages: Int = -1,
    val isbn: String = "",
    val publishDate: String = ""
)