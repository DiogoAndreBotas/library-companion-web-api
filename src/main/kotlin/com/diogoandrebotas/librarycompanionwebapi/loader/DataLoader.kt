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
        if (userRepository.findById(1).isEmpty) {
            userRepository.save(AppUser(1, "ana_rodrigues"))
        }
        if (userRepository.findById(2).isEmpty) {
            userRepository.save(AppUser(2, "diogo_botas"))
        }
    }
}