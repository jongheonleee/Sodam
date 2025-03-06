package com.backend.sodam

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SampleController {

    @GetMapping("/sample")
    fun sampleGet(): String {
        return "this is a sample [GET] test"
    }

    @PostMapping("/sample")
    fun samplePost(
        @RequestParam("name") name: String
    ): String {
        return "this is a sample test [POST] name : $name"
    }
}
