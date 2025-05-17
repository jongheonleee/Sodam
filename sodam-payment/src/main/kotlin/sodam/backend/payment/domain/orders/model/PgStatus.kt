package sodam.backend.payment.domain.orders.model

enum class PgStatus {
    CREATE,
    AUTH_SUCCESS,
    AUTH_FAILED,
    AUTH_INVALID,
    CAPTURE_REQUIRED,
    CAPTURE_RETRY,
    CAPTURE_SUCCESS,
    CAPTURE_FAILED,
}
