package com.diogoandrebotas.librarycompanionwebapi.model

import jakarta.persistence.*

@Entity
@Table(name = "users")
class AppUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val username: String = "",
    @ManyToMany
    @JoinTable(
        name = "users_books",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "isbn")]
    )
    val books: MutableList<Book> = mutableListOf()
)