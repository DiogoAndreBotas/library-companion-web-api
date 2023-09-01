package com.diogoandrebotas.librarycompanionwebapi.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
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
    @JsonIgnore
    @ManyToMany(mappedBy = "books")
    val users: MutableSet<AppUser> = mutableSetOf()
)