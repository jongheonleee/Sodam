package com.backend.sodam.global.authentication

interface Authentication {
    fun getRequestedBy(): String
}
