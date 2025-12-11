package com.example.dynasync.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dynasync.viewmodels.PaymentViewModel

@Composable
fun PaymentScreen(
    modifier: Modifier = Modifier,
    viewModel: PaymentViewModel = viewModel()
) {

    val state by viewModel.state.collectAsState()

    PaymentScreenContent(
        modifier = modifier
    )


}


@Composable
fun PaymentScreenContent(
    modifier: Modifier = Modifier
) {
    Text(text = "Screen de pagos")
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PaymentScreenPreview() {
    PaymentScreen()
}