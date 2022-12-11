package com.example.exolinkmanager.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import com.example.exolinkmanager.R

@Composable
fun NewDeeplinkCustomDialog(
    title: String? = null,
    icon: ImageVector? = null,
    value: String,
    showDialog: Boolean,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {

    val deeplinkValue = remember { mutableStateOf(value) }
    val labelValue = remember { mutableStateOf(value) }

    if (showDialog) {
        Dialog(
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                securePolicy = SecureFlagPolicy.SecureOff
            ),
            onDismissRequest = { onDismiss() }) {
            Surface(
                tonalElevation = 24.dp,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.margin_large)),
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = dimensionResource(id = R.dimen.margin_large),
                            horizontal = dimensionResource(id = R.dimen.margin_medium)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    Image(
                        imageVector = icon ?: Icons.Filled.AddCircle,
                        contentDescription = "Dialog icon",
                        alignment = Alignment.Center,
                        contentScale = ContentScale.Fit
                    )

                    Spacer(Modifier.size(dimensionResource(id = R.dimen.margin_medium)))

                    Text(
                        text = title ?: "Add a new deeplink",
                        style = MaterialTheme.typography.headlineSmall,
                    )

                    Spacer(Modifier.size(dimensionResource(id = R.dimen.margin_medium)))

                    OutlinedTextField(
                        value = deeplinkValue.value,
                        placeholder = { Text(text = "Enter complete deeplink") },
                        label = { Text(text = "Deeplink") },
                        onValueChange = { deeplinkValue.value = it },
                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius))
                    )

                    Spacer(Modifier.size(dimensionResource(id = R.dimen.margin_medium)))

                    OutlinedTextField(
                        value = labelValue.value,
                        placeholder = { Text(text = "Enter deeplink name") },
                        label = { Text(text = "Name") },
                        onValueChange = { labelValue.value = it },
                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius))
                    )

                    Spacer(Modifier.size(dimensionResource(id = R.dimen.margin_medium)))

                    Row() {
                        TextButton(onClick = onDismiss) {
                            Text(
                                text = stringResource(id = R.string.button_negative),
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }

                        TextButton(onClick = { onConfirm(deeplinkValue.value + "|" + labelValue.value) }) {
                            Text(
                                text = stringResource(id = R.string.add),
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }
        }
    }
}
