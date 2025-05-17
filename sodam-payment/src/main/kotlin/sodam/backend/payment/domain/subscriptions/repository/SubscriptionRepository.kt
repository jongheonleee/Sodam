package sodam.backend.payment.domain.subscriptions.repository

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import sodam.backend.payment.domain.subscriptions.entity.SubscriptionsEntity

@Repository
interface SubscriptionRepository: CoroutineCrudRepository<SubscriptionsEntity, String>, SubscriptionsCustomRepository
// r2dbc에서 save는 id가 널이 아니면 update 쿼리 날라감
// 따라서, 외부에서 pk 설정하고 insert 하고 싶으면 커스텀해서 사용해야함