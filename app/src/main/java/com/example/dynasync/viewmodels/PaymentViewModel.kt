package com.example.dynasync.viewmodels

import androidx.compose.runtime.internal.isLiveLiteralsEnabled
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dynasync.data.PaymentRepository
import com.example.dynasync.domain.Payment
import com.example.dynasync.domain.PaymentType
import com.example.dynasync.ui.states.PaymentViewState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PaymentViewModel: ViewModel() {
    private val _state = MutableStateFlow(value = PaymentViewState())
    val state = _state.asStateFlow()

    private var cachePayments: List<Payment> = emptyList()


    init {
        getPayments()
    }

    fun getPayments() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true )}

            try {
                delay(2000)
                val payments = PaymentRepository.getPayments()
                cachePayments = payments
                _state.update {
                    _state.value.copy(
                        isLoading = false,
                        paymentList = payments
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(isLoading = false, error = "Error al cargar el registro de pagos. Intentalo otra vez.")
                }
            }

        }
    }


    fun onFilterSelected(type: PaymentType) {
        val currentFilter = _state.value.selectedFilter
        val newFilter = if(currentFilter == type) null else type

        val filteredList = if(newFilter == null) {
            cachePayments
        } else {
            cachePayments.filter { it.paymentType == newFilter }
        }

        _state.update {
            it.copy(
                paymentList = filteredList,
                selectedFilter = newFilter
            )
        }
    }


}