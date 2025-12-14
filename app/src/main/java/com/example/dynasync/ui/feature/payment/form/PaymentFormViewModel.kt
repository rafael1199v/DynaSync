package com.example.dynasync.ui.feature.payment.form

import androidx.activity.result.launch
import androidx.compose.animation.core.copy
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.dynasync.data.repository.PaymentRepository
import com.example.dynasync.domain.model.Payment
import com.example.dynasync.domain.model.PaymentType
import com.example.dynasync.domain.model.toCustomFormat
import com.example.dynasync.navigation.MainDestination
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class PaymentFormViewModel(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _state = MutableStateFlow(PaymentFormViewState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<PaymentFormUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val paymentFormValidator = PaymentFormValidator()

    val args = savedStateHandle.toRoute<MainDestination.PaymentForm>()
    val paymentId = args.paymentId

    private var tempDate: LocalDate? = null

    init {
        if(paymentId != -1) {
            _state.update { it.copy(isEditMode = true) }
            onIntent(PaymentFormIntent.LoadPayment(paymentId))
        } else {
            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            _state.update {
                it.copy(
                    selectedDateTime = now,
                    displayDate = now.toCustomFormat()
                )
            }
        }
    }

    fun onIntent(intent: PaymentFormIntent) {
        when (intent) {
            is PaymentFormIntent.BeneficiaryChange -> _state.update { it.copy(beneficiary = intent.beneficiary) }
            is PaymentFormIntent.AmountChange -> _state.update { it.copy(amount = intent.amount) }
            is PaymentFormIntent.PaymentTypeChange -> _state.update { it.copy(paymentType = intent.paymentType) }

            PaymentFormIntent.OpenDatePicker -> _state.update { it.copy(showDatePicker = true) }
            PaymentFormIntent.DismissDatePicker -> _state.update { it.copy(showDatePicker = false) }
            is PaymentFormIntent.DateSelected -> {
                tempDate = intent.date
                _state.update { it.copy(showDatePicker = false, showTimePicker = true) }
            }

            PaymentFormIntent.DismissTimePicker -> _state.update { it.copy(showTimePicker = false) }
            is PaymentFormIntent.TimeSelected -> {
                val date = tempDate ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                val dateTime = LocalDateTime(date, intent.time)
                _state.update {
                    it.copy(
                        showTimePicker = false,
                        selectedDateTime = dateTime,
                        displayDate = dateTime.toCustomFormat()
                    )
                }
            }

            is PaymentFormIntent.LoadPayment -> loadPayment(intent.paymentId)
            is PaymentFormIntent.SubmitForm -> onSubmitForm()
        }
    }

    private fun loadPayment(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                 val payment = PaymentRepository.getPaymentById(id) // TODO: Implementar
                 _state.update {
                    it.copy(
                        beneficiary = payment.beneficiary,
                        amount = payment.amount.toString(),
                        paymentType = payment.paymentType,
                        selectedDateTime = payment.dateTime,
                        displayDate = payment.dateTime.toCustomFormat()
                    )
                 }
            } catch (e: Exception) {
                _state.update { it.copy(error = "Error al cargar el pago") }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun onSubmitForm() {
        val validationState = paymentFormValidator.validate(_state.value)

        if (paymentFormValidator.isValid(validationState)) {
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }

                try {
                    val amountDouble = _state.value.amount.toDoubleOrNull() ?: 0.0
                    val dateTime = _state.value.selectedDateTime ?: Clock.System.now().toLocalDateTime(
                        TimeZone.currentSystemDefault())

                    val payment = Payment(
                        id = if (_state.value.isEditMode) paymentId else 0,
                        beneficiary = _state.value.beneficiary,
                        amount = amountDouble,
                        paymentType = _state.value.paymentType,
                        dateTime = dateTime
                    )

                    println("payment ${payment.toString()}")

                    if (_state.value.isEditMode) {
                        PaymentRepository.updatePayment(payment) // TODO
                    } else {
                        PaymentRepository.addPayment(payment) // TODO
                    }

                    _uiEvent.send(PaymentFormUiEvent.NavigateToPayment)

                } catch (e: Exception) {
                    _state.update { it.copy(error = "Ocurrió un error al guardar.") }
                } finally {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        } else {
            _state.update { validationState }
        }
    }

    private fun onAmountChange(amount: String) {
        val split = amount.split(".")
        //TODO()
    }
}