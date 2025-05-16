package com.backend.sodam.global.authentication

import org.springframework.stereotype.Component
import java.util.*

@Component
class AuthenticationHolderImpl : AuthenticationHolder, RequestedByProvider {

    private var authentication: Authentication? = null

    override fun getAuthentication(): Optional<Authentication> {
        return Optional.ofNullable(authentication)
    }

    override fun setAuthentication(authentication: Authentication) {
        this.authentication = authentication
    }

    override fun getRequestedBy(): Optional<String> {
        return getAuthentication().map(Authentication::getRequestedBy)
    }
}
