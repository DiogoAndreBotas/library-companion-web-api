package com.diogoandrebotas.librarycompanionwebapi.loader

import com.diogoandrebotas.librarycompanionwebapi.model.AppUser
import com.diogoandrebotas.librarycompanionwebapi.repository.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class DataLoader(
    private val userRepository: UserRepository
) : ApplicationRunner {
    override fun run(args: ApplicationArguments?) {
        userRepository.saveAll(
            listOf(
                AppUser(1, "ana_rodrigues"),
                AppUser(2, "diogo_botas")
            )
        )
    }
}