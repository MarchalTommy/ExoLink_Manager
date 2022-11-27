package com.example.exolinkmanager.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import com.example.exolinkmanager.R

@Composable
fun ConfirmationAlertDialog(
    title: String? = null,
    message: String? = null,
    icon: ImageVector? = null,
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            tonalElevation = 24.dp,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                securePolicy = SecureFlagPolicy.SecureOff
            ),
            onDismissRequest = { onDismiss() },
            icon = {
                if (icon != null) {
                    Icon(imageVector = icon, contentDescription = "Dialog icon")
                }
            },
            title = {
                Text(text = title ?: "")
            },
            text = {
                Text(text = message ?: "")
            },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(
                        text = stringResource(id = R.string.button_positive),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(
                        text = stringResource(id = R.string.button_negative),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        )
    }
}
