package com.example.dynasync.ui.feature.payment

import com.example.dynasync.domain.model.Payment
import com.example.dynasync.domain.model.PaymentType

data class PaymentViewState(
    val paymentList: List<Payment> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedFilter: PaymentType? = null
)