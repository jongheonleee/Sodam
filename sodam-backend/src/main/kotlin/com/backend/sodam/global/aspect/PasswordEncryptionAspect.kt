package com.backend.sodam.global.aspect

import com.backend.sodam.global.annottion.PasswordEncryption
import lombok.RequiredArgsConstructor
import org.apache.commons.lang3.reflect.FieldUtils
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.util.ObjectUtils
import java.lang.reflect.Modifier
import java.util.*

@Aspect
@Component
@RequiredArgsConstructor
class PasswordEncryptionAspect(
    private val passwordEncoder: PasswordEncoder,
){
    @Around("execution(* com.backend.sodam.domain.users.controller..*..*(..))")
    fun passwordEncryptionAspect(pjp: ProceedingJoinPoint): Any {
        Arrays.stream(pjp.args)
              .forEach { fieldEncryption(it) }
        return pjp.proceed()
    }

    private fun fieldEncryption(obj: Any?) {
        if (ObjectUtils.isEmpty(obj)) return

        FieldUtils.getAllFieldsList(obj!!::class.java)
            .filter {
                !Modifier.isFinal(it.modifiers) && !Modifier.isStatic(it.modifiers)
            }
            .forEach { field ->
                try {
                    if (!field.isAnnotationPresent(PasswordEncryption::class.java)) return@forEach

                    val encryptionField = FieldUtils.readField(field, obj, true)

                    if (encryptionField !is String) return@forEach

                    val encryptedPassword = passwordEncoder.encode(encryptionField)
                    FieldUtils.writeField(field, obj, encryptedPassword, true)
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }
    }
}