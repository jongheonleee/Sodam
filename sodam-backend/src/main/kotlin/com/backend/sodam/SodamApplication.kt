package com.backend.sodam

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SodamApplication

fun main(args: Array<String>) {
	runApplication<SodamApplication>(*args)
}
