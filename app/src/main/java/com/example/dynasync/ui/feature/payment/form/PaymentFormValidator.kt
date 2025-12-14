package com.example.dynasync.ui.feature.payment.form

import kotlinx.datetime.LocalDateTime

class PaymentFormValidator {

    fun validate(state: PaymentFormViewState): PaymentFormViewState {
        return state.copy(
            beneficiaryError = validateBeneficiary(state.beneficiary),
            amountError = validateAmount(state.amount),
            paymentDateTimeError = validatePaymentDateTime(state.selectedDateTime)
        )
    }

    fun isValid(state: PaymentFormViewState): Boolean {
        return state.beneficiaryError == null &&
                state.amountError == null &&
                state.paymentDateTimeError == null &&
                state.beneficiary.isNotBlank() &&
                state.amount.isNotBlank() &&
                state.selectedDateTime != null // Verificamos que no sea nulo
    }

    private fun validateBeneficiary(beneficiary: String): String? {
        return when {
            beneficiary.isBlank() -> "El beneficiario es obligatorio."
            beneficiary.length < 3 -> "El nombre del beneficiario debe tener al menos 3 caracteres."
            else -> null
        }
    }

    private fun validateAmount(amount: String): String? {
        return when {
            amount.isBlank() -> "El monto es obligatorio."
            amount.toDoubleOrNull() == null -> "Por favor, introduce un monto numérico válido."
            (amount.toDoubleOrNull() ?: 0.0) <= 0.0 -> "El monto debe ser mayor que cero."
            else -> null
        }
    }

    private fun validatePaymentDateTime(dateTime: LocalDateTime?): String? {
        return if (dateTime == null) {
            "Debes seleccionar una fecha y hora para el pago."
        } else {
            null
        }
    }
}