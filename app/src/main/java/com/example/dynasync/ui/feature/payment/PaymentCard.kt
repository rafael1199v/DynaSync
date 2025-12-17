package com.example.dynasync.ui.feature.payment

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

@SuppressLint("DefaultLocale")
@Composable
fun PaymentCard(
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    payment: Payment,
    modifier: Modifier = Modifier
) {

    var showMenu by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
    ) {
        Row(
            modifier = modifier
                .clip(RoundedCornerShape(10.dp))
                .border(width = 1.5.dp, color = MaterialTheme.colorScheme.onSurface.copy(0.2f), shape = RoundedCornerShape(10.dp))
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

            Spacer(modifier = Modifier.width(20.dp))

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${String.format("%.2f", payment.amount)} Bs",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 12.dp)
                )
            }

        }


        Box(
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            IconButton(onClick = { showMenu = true }) {
                Icon(
                    painter = painterResource(R.drawable.more_vert_24),
                    contentDescription = "Opciones"
                )
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                containerColor = MaterialTheme.colorScheme.surface,
            ) {
                DropdownMenuItem(
                    text = { Text("Editar") },
                    onClick = {
                        showMenu = false
                        onEdit()
                    },
                    leadingIcon = {
                        Icon(painter = painterResource(R.drawable.outline_edit_square_24), contentDescription = null)
                    }
                )

                DropdownMenuItem(
                    text = { Text("Eliminar", color = MaterialTheme.colorScheme.error) },
                    onClick = {
                        showMenu = false
                        onDelete()
                    },
                    leadingIcon = {
                        Icon(painter = painterResource(R.drawable.delete_filled), contentDescription = null, tint = MaterialTheme.colorScheme.error)
                    }
                )
            }
        }
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
   PaymentCard(
       payment = payment,
       onDelete = {},
       onEdit = {}
   )

}