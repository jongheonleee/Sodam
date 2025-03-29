package com.backend.sodam.domain.grades.repository

import com.backend.sodam.domain.users.entity.UsersGradeEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserGradeJpaRepository: JpaRepository<UsersGradeEntity, String>, UserGradeCustomRepository