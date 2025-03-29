package com.backend.sodam.domain.grades.model

enum class GradesType(
    val desc: String,
){
    ENTRY("입문자_등급"),
    JUNIOR("초급_등급"),
    INTERMEDIATE("중급_등급"),
    ADVANCED("고급_등급"),
    MASTER("최고_등급");

    fun toRole(): String {
        return "ROLE_$name"
    }
}