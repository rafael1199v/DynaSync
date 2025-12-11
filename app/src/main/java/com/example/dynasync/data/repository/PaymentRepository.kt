package com.example.dynasync.data.repository

import com.example.dynasync.domain.model.Payment
import com.example.dynasync.domain.model.PaymentType
import kotlinx.datetime.LocalDateTime

object PaymentRepository {
    val payments: List<Payment> = listOf(
        Payment(
            id = 1,
            beneficiary = "Juan Pérez",
            amount = 1500.75,
            dateTime = LocalDateTime(2025, 3, 12, 14, 30),
            paymentType = PaymentType.PERSONAL
        ),
        Payment(
            id = 2,
            beneficiary = "Tech Solutions S.A.",
            amount = 8999.99,
            dateTime = LocalDateTime(2025, 1, 25, 10, 15),
            paymentType = PaymentType.TECHNOLOGY
        ),
        Payment(
            id = 3,
            beneficiary = "Almacén Central",
            amount = 2450.00,
            dateTime = LocalDateTime(2025, 2, 5, 9, 45),
            paymentType = PaymentType.INVENTORY
        ),
        Payment(
            id = 4,
            beneficiary = "Cuenta de Ahorros",
            amount = 500.00,
            dateTime = LocalDateTime(2025, 3, 2, 18, 0),
            paymentType = PaymentType.SAVINGS
        ),
        Payment(
            id = 5,
            beneficiary = "María López",
            amount = 320.40,
            dateTime = LocalDateTime(2025, 3, 10, 11, 20),
            paymentType = PaymentType.PERSONAL
        )
    )


    suspend fun getPayments(): List<Payment> {
        return payments
    }

}