package sodam.backend.payment.domain.common

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component
import sodam.backend.payment.domain.orders.repository.SubscriptionInOrderRepository
import sodam.backend.payment.domain.orders.service.OrderService
import sodam.backend.payment.domain.subscriptions.service.SubscriptionService
import kotlin.reflect.KClass

@Component
class Beans: ApplicationContextAware {

    companion object {
        lateinit var ctx: ApplicationContext
            private set

        fun <T: Any> getBean(byClass: KClass<T>, vararg arg: Any): T {
            return ctx.getBean(byClass.java, arg)
        }

        // 나중에 호출할 때 주입함 - 메모리 절약
        val beanSubscriptionInOrderRepository: SubscriptionInOrderRepository by lazy {
            getBean(SubscriptionInOrderRepository::class)
        }

        val beanSubscriptionService: SubscriptionService by lazy {
            getBean(SubscriptionService::class)
        }

        val beanOrderService: OrderService by lazy {
            getBean(OrderService::class)
        }

    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        ctx = applicationContext
    }
}