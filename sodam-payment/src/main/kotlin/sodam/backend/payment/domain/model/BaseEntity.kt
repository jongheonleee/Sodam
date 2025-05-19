package sodam.backend.payment.domain.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.io.Serializable
import java.time.LocalDateTime

open class BaseEntity (
    var createdBy: String = "system",
    var modifiedBy: String = "system",
    @CreatedDate
    var createdAt: LocalDateTime? = null,
    @LastModifiedDate
    var modifiedAt: LocalDateTime? = null,
): Serializable {

    override fun toString(): String {
        return "BaseEntity(createdBy='$createdBy', modifiedBy='$modifiedBy', createdAt=$createdAt, modifiedAt=$modifiedAt)"
    }
}