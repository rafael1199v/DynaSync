package com.example.dynasync.ui.feature.payment

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dynasync.R
import com.example.dynasync.domain.model.Payment
import com.example.dynasync.domain.model.PaymentType
import com.example.dynasync.domain.model.toCustomFormat
import kotlinx.datetime.LocalDateTime
import kotlin.math.roundToInt

@Composable
fun PaymentCard(
    payment: Payment,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .border(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant, shape = RoundedCornerShape(10.dp))
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

       Icon(
           painter = painterResource(id = when(payment.paymentType) {
               PaymentType.PERSONAL -> R.drawable.outline_person_apron_24
               PaymentType.INVENTORY -> R.drawable.outline_box_add_24
               PaymentType.SAVINGS -> R.drawable.outline_savings_24
               PaymentType.TECHNOLOGY -> R.drawable.outline_computer_24
           }),
           contentDescription = "Payment type Icon"
       )

        Spacer(modifier = Modifier.width(20.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterVertically)
        ) {
            Text(
                text = payment.paymentType.description,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "Beneficiado: ${payment.beneficiary}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = payment.dateTime.toCustomFormat(),
                style = MaterialTheme.typography.bodySmall
            )
        }

        Text(
            text = "${payment.amount.roundToInt()} Bs",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
        )

    }
}


@Preview(showBackground = true)
@Composable
fun PaymentCardPreview() {
    val payment = Payment(
        id = 1,
        beneficiary = "Rafael Vargas",
        amount = 500.0,
        dateTime = LocalDateTime(year = 2023, monthNumber = 1, dayOfMonth = 1, hour = 1, minute = 1),
        paymentType = PaymentType.PERSONAL
    )
   PaymentCard(payment = payment)
}