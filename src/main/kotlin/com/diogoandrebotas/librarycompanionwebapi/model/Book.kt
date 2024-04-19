package com.diogoandrebotas.librarycompanionwebapi.model

import jakarta.persistence.*

@Entity
@Table(name = "books")
class Book(
    @Id
    val isbn: String = "",
    val title: String = "",
    val pages: Int = -1,
    val publishDate: String = "",
    val imageUrl: String = "",
    @ManyToMany
    @JoinTable(
        name = "books_authors",
        joinColumns = [JoinColumn(name = "isbn")],
        inverseJoinColumns = [JoinColumn(name = "name")]
    )
    val authors: MutableList<Author> = mutableListOf()
)