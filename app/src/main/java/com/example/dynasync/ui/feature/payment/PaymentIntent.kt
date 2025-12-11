package com.example.dynasync.ui.feature.payment

import com.example.dynasync.domain.model.PaymentType

interface PaymentIntent {
    data class FilterPayments(val paymentType: PaymentType) : PaymentIntent
    data object LoadPayments : PaymentIntent
}