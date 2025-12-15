package com.example.dynasync.data.repository

import android.util.Log
import com.example.dynasync.data.dto.PaymentDto
import com.example.dynasync.data.mapper.toDomain
import com.example.dynasync.data.mapper.toDto
import com.example.dynasync.data.supabase.SupabaseClientObject
import com.example.dynasync.domain.model.Payment
import com.example.dynasync.domain.model.PaymentType
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

object PaymentRepository {

    val supabase = SupabaseClientObject.client

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


    suspend fun getPayments(userId: String): List<Payment> {
        try {
            val paymentsDto = supabase.from("payments").select(
                columns = Columns.raw("""
                    *
                """.trimIndent())
            ) {
                filter {
                    eq("profile_id", userId)
                }
            }.decodeList<PaymentDto>()

            return paymentsDto.map { it.toDomain() }

        }
        catch (e: Exception) {
            Log.e("main", "Hubo un error al cargar los pagos. ${e}")
            return emptyList()
        }
    }

    suspend fun getPaymentById(id: Int): Payment {
        delay(500)
        return payments.find { it.id == id }
            ?: throw Exception("El pago con ID $id no fue encontrado.")
    }

    suspend fun addPayment(payment: Payment) {
        try {
            val newPayment = payment.copy(
                createdAt = Clock.System.now()
            )

            supabase.from("payments").insert(newPayment.toDto())
        }
        catch (e: Exception) {
            Log.e("main", "Hubo un error al cargar los pagos. ${e}")
            throw e
        }

    }

    suspend fun updatePayment(updatedPayment: Payment) {
        delay(1000)

        val index = payments.indexOfFirst { it.id == updatedPayment.id }

        if (index != -1) {
            val currentPayment = payments[index]
            payments[index] = updatedPayment.copy(createdAt = currentPayment.createdAt)
        } else {
            throw Exception("No se pudo actualizar: El pago no existe.")
        }
    }

    suspend fun deletePayment(id: Int) {
        delay(500)
        val wasRemoved = payments.removeIf { it.id == id }

        if (!wasRemoved) {
            throw Exception("No se pudo eliminar: El pago no existe.")
        }
    }

}