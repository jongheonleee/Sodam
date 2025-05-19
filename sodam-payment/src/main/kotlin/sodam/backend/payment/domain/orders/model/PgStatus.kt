package sodam.backend.payment.domain.orders.model

enum class PgStatus {
    CREATE,
    PAID,
    AUTH_SUCCESS,
    AUTH_FAILED,
    AUTH_INVALID,
    CAPTURE_REQUEST,
    CAPTURE_REQUIRED,
    CAPTURE_RETRY,
    CAPTURE_SUCCESS,
    CAPTURE_FAILED,
}
