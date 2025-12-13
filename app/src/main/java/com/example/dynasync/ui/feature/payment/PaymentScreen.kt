package com.example.dynasync.ui.feature.payment

import androidx.compose.foundation.Image
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dynasync.R
import com.example.dynasync.data.repository.PaymentRepository
import com.example.dynasync.domain.model.PaymentType
import com.example.dynasync.ui.theme.JungleTeal

@Composable
fun PaymentScreen(
    modifier: Modifier = Modifier,
    viewModel: PaymentViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    if(state.isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
    else {
        PaymentScreenContent(
            state = state,
            onIntent = { intent ->
                viewModel.onIntent(intent)
            },
            modifier = modifier
        )
    }

}


@Composable
fun PaymentScreenContent(
    state: PaymentViewState,
    onIntent: (PaymentIntent) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        if (state.error != null) {
            Column(
                modifier = Modifier.fillMaxSize().padding(28.dp)
            ) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }

        } else if (state.paymentList.isNotEmpty()) {
            LazyRow(
                modifier = Modifier,
                contentPadding = PaddingValues(horizontal = 36.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = PaymentType.entries.toTypedArray()) { paymentType ->

                    val isSelected = state.selectedFilter == paymentType

                    FilterChip(
                        onClick = { onIntent(PaymentIntent.FilterPayments(paymentType)) },
                        label = {
                            Text(text = paymentType.description)
                        },
                        selected = isSelected,
                        leadingIcon = if (isSelected) {
                            {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_check_24),
                                    contentDescription = null,
                                )
                            }
                        } else null,
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.Transparent,
                            labelColor = Color.Black,
                            iconColor = Color.Gray,

                            selectedContainerColor = JungleTeal,
                            selectedLabelColor = Color.White,
                            selectedLeadingIconColor = Color.White,
                        )
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(
                    top = 20.dp,
                    start = 32.dp,
                    end = 32.dp,
                    bottom = 60.dp
                ),

                ) {
                items(items = state.paymentList) { payment ->
                    PaymentCard(payment = payment, modifier.fillMaxWidth())
                }
            }

        } else {

            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.no_payments_2),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(294.dp),
                )

                Text(
                    text = "No tienes ningún pago registrado. Añade uno nuevo!",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
            }

        }

    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PaymentScreenPreview() {
    PaymentScreenContent(
        state = PaymentViewState(paymentList = PaymentRepository.payments),
        onIntent = {},
        modifier = Modifier.fillMaxSize()
    )
}