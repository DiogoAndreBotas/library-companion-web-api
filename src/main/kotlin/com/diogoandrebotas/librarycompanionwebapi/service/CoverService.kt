package com.diogoandrebotas.librarycompanionwebapi.service

import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import java.util.*

@Service
class CoverService(
    private val s3Service: S3Service
) {
    fun getCover(isbn: String): Optional<ByteArray> {
        return runBlocking {
            s3Service.getImage(isbn)
        }
    }
}