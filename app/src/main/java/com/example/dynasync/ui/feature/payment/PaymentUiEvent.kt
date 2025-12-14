package com.example.dynasync.ui.feature.payment

interface PaymentUiEvent {
    data class NavigateToPaymentForm(val paymentId: Int) : PaymentUiEvent
}