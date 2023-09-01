package com.diogoandrebotas.librarycompanionwebapi.model

import jakarta.persistence.*

@Entity
@Table(name = "users")
class AppUser(
    @Id
    val username: String = "",
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_books",
        joinColumns = [JoinColumn(name = "username")],
        inverseJoinColumns = [JoinColumn(name = "isbn")]
    )
    val books: MutableSet<Book> = mutableSetOf()
)