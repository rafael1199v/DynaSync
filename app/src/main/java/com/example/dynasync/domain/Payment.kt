package com.example.dynasync.domain

import java.util.Locale
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

fun LocalDateTime.toCustomFormat(): String {
    val monthName = when (this.monthNumber) {
        1 -> "enero"
        2 -> "febrero"
        3 -> "marzo"
        4 -> "abril"
        5 -> "mayo"
        6 -> "junio"
        7 -> "julio"
        8 -> "agosto"
        9 -> "septiembre"
        10 -> "octubre"
        11 -> "noviembre"
        12 -> "diciembre"
        else -> ""
    }.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

    val formattedMinute = this.minute.toString().padStart(2, '0')

    return "${this.dayOfMonth} de $monthName de ${this.year}, ${this.hour}:${formattedMinute}"
}

