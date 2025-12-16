package com.example.dynasync.ui.feature.payment

import com.example.dynasync.domain.model.PaymentType

sealed interface PaymentIntent {
    data class FilterPayments(val paymentType: PaymentType?) : PaymentIntent
    data object LoadPayments : PaymentIntent

    data class EditPayment(val paymentId: Int): PaymentIntent

    data class DeletePayment(val paymentId: Int): PaymentIntent
}