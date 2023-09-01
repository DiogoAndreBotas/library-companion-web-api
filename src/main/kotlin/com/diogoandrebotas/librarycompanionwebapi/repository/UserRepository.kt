package com.diogoandrebotas.librarycompanionwebapi.repository

import com.diogoandrebotas.librarycompanionwebapi.model.AppUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<AppUser, String>