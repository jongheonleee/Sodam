package com.backend.sodam.global.http.kako

import com.backend.sodam.domain.users.model.SodamUser
import com.backend.sodam.global.port.KakaoUserPort
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
@RequiredArgsConstructor
class KakaoUserHttpClient : KakaoUserPort {
    companion object {
        const val KAKAO_USERINFO_API_URL = "https://kapi.kakao.com/v2/user/me"
    }

    override fun findUserFromKakao(accessToken: String): SodamUser {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()

        headers.add(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")

        val entity = HttpEntity<String>(headers)
        val response = restTemplate.exchange(
            KAKAO_USERINFO_API_URL,
            HttpMethod.GET,
            entity,
            Map::class.java
        )

        val properties = response.body?.get("properties") as Map<*, *>
        val nickname = properties["nickname"] as String
        val providerId = response.body?.get("id") as Long

        return SodamUser(
            username = nickname,
            providerId = providerId.toString()
        )
    }
}
