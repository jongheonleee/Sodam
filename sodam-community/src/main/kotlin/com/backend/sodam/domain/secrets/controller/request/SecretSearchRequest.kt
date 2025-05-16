package com.backend.sodam.domain.secrets.controller.request

import com.backend.sodam.domain.secrets.service.command.SecretSearchCommand
import org.springframework.web.bind.annotation.RequestParam

data class SecretSearchRequest(
    @RequestParam
    val title: String? = null,
    @RequestParam
    val author: String? = null
) {
    fun toCommand(): SecretSearchCommand {
        return SecretSearchCommand(
            title = title,
            author = author
        )
    }
}
