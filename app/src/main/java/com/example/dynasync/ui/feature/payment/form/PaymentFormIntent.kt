package com.example.dynasync.ui.feature.payment.form

import com.example.dynasync.domain.model.PaymentType
import com.example.dynasync.ui.feature.profile.ProfileIntent
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

sealed interface PaymentFormIntent {
    data class BeneficiaryChange(val beneficiary: String) : PaymentFormIntent
    data class AmountChange(val amount: String) : PaymentFormIntent
    data class PaymentTypeChange(val paymentType: PaymentType) : PaymentFormIntent

    data object OpenDatePicker : PaymentFormIntent
    data object DismissDatePicker : PaymentFormIntent
    data class DateSelected(val date: LocalDate) : PaymentFormIntent

    data object DismissTimePicker : PaymentFormIntent
    data class TimeSelected(val time: LocalTime) : PaymentFormIntent

    data class LoadPayment(val paymentId: Int) : PaymentFormIntent

    data object CleanError: PaymentFormIntent
    data object SubmitForm : PaymentFormIntent
}