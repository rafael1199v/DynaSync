package com.example.dynasync.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.dynasync.ui.feature.createproject.CreateProjectIntent
import com.example.dynasync.ui.theme.JungleTeal


@Composable
fun DynaSyncTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorMessage: String?,
    supportingText: String,
    charCount: Int,
    maxChars: Int,
    modifier: Modifier = Modifier,
    maxLines: Int = 1
) {


    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        modifier = modifier,
        isError = errorMessage != null,
        label = {
            Text(text = label)
        },
        supportingText = {
            if(errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }
            else {
                Text(text = "${supportingText} - ${charCount}/${maxChars}")
            }
        },
        maxLines = maxLines
    )
}