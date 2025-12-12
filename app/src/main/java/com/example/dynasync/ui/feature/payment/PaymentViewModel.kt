package com.example.dynasync.ui.feature.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dynasync.data.repository.PaymentRepository
import com.example.dynasync.domain.model.Payment
import com.example.dynasync.domain.model.PaymentType
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
        onIntent(PaymentIntent.LoadPayments)
    }


    fun onIntent(intent: PaymentIntent) {
        when(intent) {
            is PaymentIntent.FilterPayments -> {
                onFilterSelected(intent.paymentType)
            }
            PaymentIntent.LoadPayments -> {
                getPayments()
            }
        }
    }

    private fun getPayments() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true )}

            try {

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


    private fun onFilterSelected(type: PaymentType) {
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