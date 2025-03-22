package com.backend.sodam.domain.secrets.controller.request

import com.backend.sodam.domain.secrets.service.command.SecretCreateCommand

data class SecretCreateRequest(
    val title: String,
) {
    fun toCommand() : SecretCreateCommand {
        return SecretCreateCommand(
            title = title
        )
    }
}
