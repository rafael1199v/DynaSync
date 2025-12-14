package com.example.dynasync.data.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class PaymentDto(
    @SerialName("id")
    val id: Int? = null,

    @SerialName("beneficiary")
    val beneficiary: String,

    @SerialName("amount")
    val amount: Double,

    @SerialName("date_time")
    val dateTime: LocalDateTime,

    @SerialName("payment_type")
    val paymentType: PaymentTypeDto,

    @SerialName("created_at")
    val createdAt: String? = null
)

@Serializable
enum class PaymentTypeDto {
    @SerialName("PERSONAL") PERSONAL,
    @SerialName("INVENTORY") INVENTORY,
    @SerialName("SAVINGS") SAVINGS,
    @SerialName("TECHNOLOGY") TECHNOLOGY
}
