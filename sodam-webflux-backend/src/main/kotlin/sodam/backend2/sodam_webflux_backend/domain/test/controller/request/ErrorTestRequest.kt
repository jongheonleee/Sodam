package sodam.backend2.sodam_webflux_backend.domain.test.controller.request

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import sodam.backend2.sodam_webflux_backend.golbal.annotation.DateString

data class ErrorTestRequest(
    @field:NotEmpty
    @field:Size(min=3, max=10)
    val id: String?,

    @field:NotNull
    @field:Positive(message = "양수만 입력 가능합니다.")
    @field:Max(150)
    val age: Int?,

    @field:DateString
    val birthday: String?,

    val message: String? = null
)
