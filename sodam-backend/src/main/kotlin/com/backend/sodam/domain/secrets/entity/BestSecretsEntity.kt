package com.backend.sodam.domain.secrets.entity

import com.backend.sodam.global.audit.MutableBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import lombok.AccessLevel
import lombok.NoArgsConstructor

@Entity
@Table(name = "best_secrets")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class BestSecretsEntity(
    @Id
    @Column(name = "BEST_SECRET_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val bestSecretId: Long? = null,

    @ManyToOne
    @JoinColumn(name = "SECRETE_ID")
    var secret: SecretsEntity? = null,

    @Column(name = "SECRET_TITLE")
    val secretTitle: String,

    @Column(name = "SECRET_SUMMARY")
    val secretSummary: String,

    @Column(name = "SECRET_AUTHOR")
    val secretAuthor: String,

    @Column(name = "TAG1")
    val tag1: String,

    @Column(name = "TAG2")
    val tag2: String,

    @Column(name = "TAG3")
    val tag3: String,

): MutableBaseEntity() {
}