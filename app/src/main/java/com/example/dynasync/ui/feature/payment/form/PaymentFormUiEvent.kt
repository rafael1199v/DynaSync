package com.example.dynasync.ui.feature.payment.form

sealed interface PaymentFormUiEvent {
    data object NavigateToPayment : PaymentFormUiEvent
}