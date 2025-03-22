package com.backend.sodam.global.http.kako

import com.backend.sodam.global.port.KakaoTokenPort
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

@Component
@RequiredArgsConstructor
class KakaoTokenHttpClient(
    @Value("\${spring.security.oauth2.client.registration.kakao.client-id}")
    private val kakaoClientId: String,

    @Value("\${spring.security.oauth2.client.registration.kakao.client-secret}")
    private val kakaoClientSecret: String,

    @Value("\${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private val kakaoRedirectUri: String
) : KakaoTokenPort {

    companion object {
        private const val KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token"
    }

    override fun getAccessTokenByCode(code: String): String {
        val restTemplate = RestTemplate()
        val params = LinkedMultiValueMap<String, String>()

        params.add("grant_type", "authorization_code")
        params.add("client_id", kakaoClientId)
        params.add("client_secret", kakaoClientSecret)
        params.add("redirect_uri", kakaoRedirectUri)
        params.add("code", code)

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val request = HttpEntity<MultiValueMap<String, String>>(params, headers)

        val exchange = restTemplate.exchange(
            KAKAO_TOKEN_URL,
            HttpMethod.POST,
            request,
            Map::class.java
        )

        return exchange.body?.get("access_token") as String
    }
}
