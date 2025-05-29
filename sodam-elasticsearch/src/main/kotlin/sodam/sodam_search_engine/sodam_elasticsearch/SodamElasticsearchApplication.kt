package sodam.sodam_search_engine.sodam_elasticsearch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SodamElasticsearchApplication

fun main(args: Array<String>) {
	runApplication<SodamElasticsearchApplication>(*args)
}
