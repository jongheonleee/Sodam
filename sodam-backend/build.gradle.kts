plugins {
    kotlin("jvm") version "1.9.25" // kotlin을 JVM에서 실행하기 위한 기본 플러그인
    kotlin("plugin.spring") version "1.9.25" // kotlin과 스프링 프레임워크를 함께 사용하기 위한 플러그인 -> 빈 설정, 의존성 주입 ..
    kotlin("plugin.jpa") version "1.9.25" // kotlin과 jpa를 함께 사용할 때 발생하는 문제를 해결하기 위한 플러그인
    // - kotlin 의 클래스는 기본적으로 final이 붙기 때문에 확장 x. 이는 jpa의 엔티티를 정의할 때 지연로딩을 위한 프록시 객체 생성을 할 수 없음
    // => 매번 open 변경자를 붙어야함. 하지만,  kotlin("plugin.jpa")을 사용하면 all-open 플러그인을 통해 엔티티 클래스를 정의할 때 open 변경자를 자동으로 추가해줌
    // => 밑에 allOpen 설정 참고

    kotlin("kapt") version "1.8.22" // QueryDSL 의존성 추가

    id("org.springframework.boot") version "3.3.9"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.jlleitschuh.gradle.ktlint") version "11.4.0" // ktlint 플러그인 추가
}

group = "com.backend"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    compileOnly("org.projectlombok:lombok")
    testImplementation("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // kotest 의존성 추가
    testImplementation("io.kotest:kotest-runner-junit5:5.6.2")
    testImplementation("io.kotest:kotest-assertions-core:5.6.2")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.3")

    // swagger 의존성 추가
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")

    // QueryDSL 의존성 추가
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")

    // redis 의존성 추가
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // audit 관련 의존성 추가(AuditorAware)
    implementation("org.springframework.data:spring-data-commons")

    // 스프링 시큐리티 관련 의존성 추가
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.security:spring-security-oauth2-client")
    testImplementation("org.springframework.security:spring-security-test")

    // flyway 관련 의존성 추가
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")

    // MockK 의존성 추가
    testImplementation("io.mockk:mockk:1.13.5") // MockK 라이브러리 추가
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val appMainClassName = "com.backend.sodam.SodamApplication"
tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    mainClass.set(appMainClassName)
    archiveClassifier.set("boot")
}
