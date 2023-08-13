package com.diogoandrebotas.librarycompanionwebapi.service

import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class CoverService(
    private val s3Service: S3Service
) {
    fun getCover(isbn: String): ByteArray? {
        return runBlocking {
            s3Service.getImage(isbn)
        }
    }
}