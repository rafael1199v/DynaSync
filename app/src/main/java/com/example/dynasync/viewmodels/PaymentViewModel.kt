package com.example.dynasync.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dynasync.data.PaymentRepository
import com.example.dynasync.ui.states.PaymentViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PaymentViewModel: ViewModel() {
    private val _state = MutableStateFlow(value = PaymentViewState())
    val state = _state.asStateFlow()

    init {
        getPayments()
    }

    fun getPayments() {
        viewModelScope.launch {
            val payments = PaymentRepository.getPayments()
            _state.update {
                _state.value.copy(
                    paymentList = payments
                )
            }
        }
    }

}