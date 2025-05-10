package sodam.backend2.sodam_webflux_backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing

@SpringBootApplication
@EnableR2dbcAuditing
class SodamWebfluxBackendApplication

fun main(args: Array<String>) {
	runApplication<SodamWebfluxBackendApplication>(*args)
}
