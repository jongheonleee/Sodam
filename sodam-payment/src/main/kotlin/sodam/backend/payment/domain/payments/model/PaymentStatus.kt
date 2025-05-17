package sodam.backend.payment.domain.payments.model

enum class PaymentStatus {
    CANCELLED,
    COMPLETED,
    FAILED,
    CREATED,
    PENDING,
}