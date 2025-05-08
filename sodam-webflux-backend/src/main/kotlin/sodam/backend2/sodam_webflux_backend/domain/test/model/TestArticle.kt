package sodam.backend2.sodam_webflux_backend.domain.test.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime


// R2DBC의 경우, 영속성 컨텍스트가 없음
// JPA의 관계 매핑이 없음. 순수하게 매핑하기만 함
@Table(name = "TB_ARTICLE")
class TestArticle(
    @Id
    var id: Long = 0,
    var title: String,
    var body: String? = null,
    var authorId: Long? = null,
): BaseEntity() {


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TestArticle
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "Article(id=$id, title='$title', body='$body', authorId=$authorId), ${super.toString()}"
    }
}


open class BaseEntity(
    @CreatedDate
    var createdAt: LocalDateTime? = null,
    @LastModifiedDate
    var updatedAt: LocalDateTime? = null
) {

    override fun toString(): String {
        return "createdAt=$createdAt, updatedAt=$updatedAt"
    }
}