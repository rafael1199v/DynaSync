package com.example.dynasync.ui.feature.payment.form

import com.example.dynasync.domain.model.PaymentType
import kotlinx.datetime.LocalDateTime

data class PaymentFormViewState(
    val beneficiary: String = "",
    val amount: String = "",
    val paymentType: PaymentType = PaymentType.PERSONAL,

    val selectedDateTime: LocalDateTime? = null,
    val displayDate: String = "",

    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,

    val beneficiaryError: String? = null,
    val amountError: String? = null,
    val paymentDateTimeError: String? = null,

    val isLoading: Boolean = false,
    val error: String? = null,
    val isEditMode: Boolean = false
)