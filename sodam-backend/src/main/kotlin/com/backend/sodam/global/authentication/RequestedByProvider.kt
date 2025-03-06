package com.backend.sodam.global.authentication

import java.util.*

interface RequestedByProvider {
    fun getRequestedBy(): Optional<String>
}
