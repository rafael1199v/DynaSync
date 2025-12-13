package com.example.dynasync.data.repository

import com.example.dynasync.domain.model.Payment
import com.example.dynasync.domain.model.PaymentType
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDateTime
import kotlin.time.Instant

object PaymentRepository {
    val payments: MutableList<Payment> = mutableListOf(
        Payment(
            id = 1,
            beneficiary = "Juan Pérez",
            amount = 1500.75,
            dateTime = LocalDateTime(2025, 3, 12, 14, 30),
            paymentType = PaymentType.PERSONAL,
            createdAt = Instant.parse("2023-11-01T12:00:00Z")
        ),
        Payment(
            id = 2,
            beneficiary = "Tech Solutions S.A.",
            amount = 8999.99,
            dateTime = LocalDateTime(2025, 1, 25, 10, 15),
            paymentType = PaymentType.TECHNOLOGY,
            createdAt = Instant.parse("2023-11-01T12:00:00Z")
        ),
        Payment(
            id = 3,
            beneficiary = "Almacén Central",
            amount = 2450.00,
            dateTime = LocalDateTime(2025, 2, 5, 9, 45),
            paymentType = PaymentType.INVENTORY,
            createdAt = Instant.parse("2023-11-01T12:00:00Z")
        ),
        Payment(
            id = 4,
            beneficiary = "Cuenta de Ahorros",
            amount = 500.00,
            dateTime = LocalDateTime(2025, 3, 2, 18, 0),
            paymentType = PaymentType.SAVINGS,
            createdAt = Instant.parse("2023-11-01T12:00:00Z")
        ),
        Payment(
            id = 5,
            beneficiary = "María López",
            amount = 320.40,
            dateTime = LocalDateTime(2025, 3, 10, 11, 20),
            paymentType = PaymentType.PERSONAL,
            createdAt = Instant.parse("2023-11-01T12:00:00Z")
        )
    )


    suspend fun getPayments(): List<Payment> {
        delay(2000)
        return payments
    }

}