package com.backend.sodam.global.audit

import java.util.Optional

interface RequestedByProvider {
    fun getRequestedBy(): Optional<String>
}
