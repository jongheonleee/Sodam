package sodam.sodam_search_engine.sodam_elasticsearch

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import sodam.backend.payment.domain.orders.model.PgStatus
import sodam.sodam_search_engine.sodam_elasticsearch.domain.orders.entity.OrdersHistoryEntity
import sodam.sodam_search_engine.sodam_elasticsearch.domain.orders.repository.OrdersHistoryRepository
import sodam.sodam_search_engine.sodam_elasticsearch.global.extension.toLocalDate
import java.time.LocalDateTime

@SpringBootTest
class SodamElasticsearchApplicationTests(
	@Autowired private val repository: OrdersHistoryRepository,
) {

	@Test
	fun contextLoads() {
		runBlocking {
			listOf(
				OrdersHistoryEntity("1-test1",  "user1", null,  50_000,  5_000,  45_000,  "bronze * 3", PgStatus.CAPTURE_REQUEST, "2025-01-01".toLocalDateTime()),
				OrdersHistoryEntity("1-test2",  "user1", null,  60_000, 10_000,  50_000,  "silver * 2", PgStatus.CAPTURE_REQUEST, "2025-01-02".toLocalDateTime()),
				OrdersHistoryEntity("1-test3",  "user1", null,  70_000,  7_000,  63_000,  "silver * 3", PgStatus.CAPTURE_REQUEST, "2025-01-03".toLocalDateTime()),
				OrdersHistoryEntity("1-test4",  "user1", null, 100_000, 25_000,  75_000,  "gold * 1",   PgStatus.CAPTURE_REQUEST, "2025-01-04".toLocalDateTime()),
				OrdersHistoryEntity("1-test5",  "user1", null,  30_000,  3_000,  27_000,  "bronze * 1", PgStatus.CAPTURE_REQUEST, "2025-01-05".toLocalDateTime()),
				OrdersHistoryEntity("1-test6",  "user1", null, 120_000, 20_000, 100_000,  "gold * 2",   PgStatus.CAPTURE_REQUEST, "2025-01-06".toLocalDateTime()),
				OrdersHistoryEntity("1-test7",  "user1", null,  90_000, 15_000,  75_000,  "gold * 1",   PgStatus.CAPTURE_REQUEST, "2025-01-07".toLocalDateTime()),
				OrdersHistoryEntity("1-test8",  "user1", null,  40_000,  5_000,  35_000,  "bronze * 2", PgStatus.CAPTURE_REQUEST, "2025-01-08".toLocalDateTime()),
				OrdersHistoryEntity("1-test9",  "user1", null,  55_000,  2_000,  53_000,  "silver * 1", PgStatus.CAPTURE_REQUEST, "2025-01-09".toLocalDateTime()),
				OrdersHistoryEntity("1-test10", "user1", null,  65_000,  6_500,  58_500,  "silver * 3", PgStatus.CAPTURE_REQUEST, "2025-01-10".toLocalDateTime()),
				OrdersHistoryEntity("1-test11", "user1", null,  80_000, 10_000,  70_000,  "gold * 1",   PgStatus.CAPTURE_REQUEST, "2025-01-11".toLocalDateTime()),

				OrdersHistoryEntity("2-test1",  "user2", null,  50_000,  5_000,  45_000,  "bronze * 3", PgStatus.CAPTURE_REQUEST, "2025-02-01".toLocalDateTime()),
				OrdersHistoryEntity("2-test2",  "user2", null,  60_000, 10_000,  50_000,  "silver * 2", PgStatus.CAPTURE_REQUEST, "2025-02-02".toLocalDateTime()),
				OrdersHistoryEntity("2-test3",  "user2", null,  70_000,  7_000,  63_000,  "silver * 3", PgStatus.CAPTURE_REQUEST, "2025-02-03".toLocalDateTime()),
				OrdersHistoryEntity("2-test4",  "user2", null, 100_000, 25_000,  75_000,  "gold * 1",   PgStatus.CAPTURE_REQUEST, "2025-02-04".toLocalDateTime()),
				OrdersHistoryEntity("2-test5",  "user2", null,  30_000,  3_000,  27_000,  "bronze * 1", PgStatus.CAPTURE_REQUEST, "2025-02-05".toLocalDateTime()),
				OrdersHistoryEntity("2-test6",  "user2", null, 120_000, 20_000, 100_000,  "gold * 2",   PgStatus.CAPTURE_REQUEST, "2025-02-06".toLocalDateTime()),
				OrdersHistoryEntity("2-test7",  "user2", null,  90_000, 15_000,  75_000,  "gold * 1",   PgStatus.CAPTURE_REQUEST, "2025-02-07".toLocalDateTime()),
				OrdersHistoryEntity("2-test8",  "user2", null,  40_000,  5_000,  35_000,  "bronze * 2", PgStatus.CAPTURE_REQUEST, "2025-02-08".toLocalDateTime()),
				OrdersHistoryEntity("2-test9",  "user2", null,  55_000,  2_000,  53_000,  "silver * 1", PgStatus.CAPTURE_REQUEST, "2025-02-09".toLocalDateTime()),
				OrdersHistoryEntity("2-test10", "user2", null,  65_000,  6_500,  58_500,  "silver * 3", PgStatus.CAPTURE_REQUEST, "2025-02-10".toLocalDateTime()),
				OrdersHistoryEntity("2-test11", "user2", null,  80_000, 10_000,  70_000,  "gold * 1",   PgStatus.CAPTURE_REQUEST, "2025-02-11".toLocalDateTime())
			).forEach {
				repository.save(it)
			}
		}
	}

}


fun String.toLocalDateTime(): LocalDateTime {
	return this.toLocalDate().atStartOfDay()
}