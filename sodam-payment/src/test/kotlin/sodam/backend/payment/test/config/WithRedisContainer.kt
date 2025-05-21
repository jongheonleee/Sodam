package sodam.backend.payment.test.config

import mu.KotlinLogging
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

private val logger = KotlinLogging.logger {}

interface WithRedisContainer {
    companion object {
        private val container = GenericContainer(DockerImageName.parse("redis:alpine")).apply {
            addExposedPorts(6379)
            start()
        }

        @JvmStatic
        @DynamicPropertySource
        fun setProperty(registry: DynamicPropertyRegistry) {
            logger.debug { "redis mapped port: ${container.getMappedPort(6379)}" }
            registry.add("spring.data.redis.port") {
                "${container.getMappedPort(6379)}"
            }
        }
    }
}
