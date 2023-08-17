package com.diogoandrebotas.librarycompanionwebapi.config

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.s3.S3Client
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class S3Config {

    @Value("\${aws.accessKey}")
    private lateinit var accessKey: String

    @Value("\${aws.secretKey}")
    private lateinit var secretKey: String

    @Value("\${aws.region}")
    private lateinit var instanceRegion: String

    @Bean
    fun client() = S3Client {
        region = instanceRegion
        credentialsProvider = StaticCredentialsProvider {
            accessKeyId = accessKey
            secretAccessKey = secretKey
        }
    }
}
