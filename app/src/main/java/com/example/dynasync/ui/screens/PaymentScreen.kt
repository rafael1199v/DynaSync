package com.example.dynasync.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dynasync.data.PaymentRepository
import com.example.dynasync.domain.PaymentType
import com.example.dynasync.ui.payment.PaymentCard
import com.example.dynasync.ui.states.PaymentViewState
import com.example.dynasync.viewmodels.PaymentViewModel

@Composable
fun PaymentScreen(
    modifier: Modifier = Modifier,
    viewModel: PaymentViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    PaymentScreenContent(
        state = state,
        modifier = modifier
    )


}


@Composable
fun PaymentScreenContent(
    state: PaymentViewState,
    modifier: Modifier = Modifier
) {
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            modifier = Modifier,
            contentPadding = PaddingValues(horizontal = 36.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = PaymentType.entries.toTypedArray()) { paymentType ->

                FilterChip(
                    onClick = {},
                    label = {
                        Text(text = paymentType.description)
                    },
                    selected = false,
                    leadingIcon = {}
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(
                top = 12.dp,
                start = 32.dp,
                end = 32.dp,
                bottom = 60.dp
            ),

        ) {
            items(items = state.paymentList) { payment ->
                PaymentCard(payment = payment, modifier.fillMaxWidth())
            }
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PaymentScreenPreview() {
    PaymentScreenContent(state = PaymentViewState(paymentList = PaymentRepository.payments),modifier = Modifier.fillMaxSize())
}