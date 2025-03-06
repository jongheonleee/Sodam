package com.backend.sodam.global.authentication

class RequestedBy(
    private val requestedBy: String
) : Authentication {

    override fun getRequestedBy(): String {
        return requestedBy
    }
}
