package sodam.backend2.sodam_webflux_backend.golbal.aspect

import kotlinx.coroutines.slf4j.MDCContext
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.core.KotlinDetector
import org.springframework.stereotype.Component
import kotlin.coroutines.Continuation

@Aspect
@Component
class WrapContextAspect {

    @Around("""
        @annotation(org.springframework.web.bind.annotation.RequestMapping) ||
        @annotation(org.springframework.web.bind.annotation.GetMapping) ||
        @annotation(org.springframework.web.bind.annotation.PostMapping) || 
        @annotation(org.springframework.web.bind.annotation.PutMapping) || 
        @annotation(org.springframework.web.bind.annotation.DeleteMapping) ||
        @annotation(org.springframework.web.bind.annotation.PatchMapping)
    """)
    fun bindMdcContext(pjp: ProceedingJoinPoint): Any? {
        return if (pjp.hasSuspendFunction) {
            val continuation = pjp.args.last() as Continuation<*>
            val newContext = continuation.context + MDCContext()
            val newContinuation = Continuation(newContext) { continuation.resumeWith(it) }
            val newArgs = pjp.args.dropLast(1) + newContinuation
            pjp.proceed(newArgs.toTypedArray())
        } else {
            pjp.proceed()
        }
    }

    private val ProceedingJoinPoint.hasSuspendFunction: Boolean
        get() {
            val method = (this.signature as MethodSignature).method
            return KotlinDetector.isSuspendingFunction(method)
        }
}