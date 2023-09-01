package com.diogoandrebotas.librarycompanionwebapi.loader

import com.diogoandrebotas.librarycompanionwebapi.model.AppUser
import com.diogoandrebotas.librarycompanionwebapi.repository.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class UserLoader(
    private val userRepository: UserRepository
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        // Universal user, temporary
        userRepository.save(AppUser("test_user"))
    }
}