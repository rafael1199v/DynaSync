package com.example.dynasync.domain

import kotlinx.datetime.LocalDateTime

data class Payment(
    val id: Int,
    val beneficiary: String,
    val amount: Double,
    val dateTime: LocalDateTime,
    val paymentType: PaymentType
)

enum class PaymentType(val description: String) {
    PERSONAL(description = "Personal"),
    INVENTORY(description = "Inventario"),
    SAVINGS(description = "Ahorros"),
    TECHNOLOGY(description = "Tecnología")
}

