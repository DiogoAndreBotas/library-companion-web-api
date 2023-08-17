package com.diogoandrebotas.librarycompanionwebapi.controller

import com.diogoandrebotas.librarycompanionwebapi.service.CoverService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class CoverController(
    private val coverService: CoverService
) {
    @GetMapping("/covers/{isbn}", produces = ["image/jpeg"])
    fun getCover(@PathVariable isbn: String): ResponseEntity<ByteArray> {
        val cover = coverService.getCover(isbn)

        return if (cover.isEmpty)
            ResponseEntity.notFound().build()
        else
            ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(cover.get())
    }
}