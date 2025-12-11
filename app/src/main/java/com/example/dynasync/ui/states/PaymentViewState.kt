package com.example.dynasync.ui.states

import com.example.dynasync.domain.Payment
import com.example.dynasync.domain.PaymentType

data class PaymentViewState(
    val paymentList: List<Payment> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedFilter: PaymentType? = null
)
