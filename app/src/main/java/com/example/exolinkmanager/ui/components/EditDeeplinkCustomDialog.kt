package com.example.exolinkmanager.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
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
import com.example.exolinkmanager.ui.models.Deeplink
import com.example.exolinkmanager.ui.models.IS_INTERNAL_KEY
import com.example.exolinkmanager.ui.models.LABEL_KEY
import com.example.exolinkmanager.ui.models.PATH_KEY
import com.example.exolinkmanager.ui.models.SCHEMA_KEY
import com.example.exolinkmanager.ui.models.buildDeeplinkObject
import com.example.exolinkmanager.ui.models.buildFinalDeeplink
import com.example.exolinkmanager.ui.models.extractValuesFromDeeplink

@Composable
fun EditDeeplinkCustomDialog(
    title: String? = null,
    icon: ImageVector? = null,
    deeplink: Deeplink?,
    showDialog: Boolean,
    onConfirm: (Deeplink) -> Unit,
    onDismiss: () -> Unit
) {

    /**
     * No "remember" here because it caused the dialog to never forget it's first value,
     * thus only showing the first deeplink you tried to edit.
     */
    var labelValue = deeplink?.label ?: ""
    var deeplinkValue = deeplink.buildFinalDeeplink()

    if (showDialog) {
        Dialog(
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                securePolicy = SecureFlagPolicy.SecureOff
            ),
            onDismissRequest = {
                onDismiss()
            }) {
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
                        imageVector = icon ?: Icons.Filled.Edit,
                        contentDescription = "Dialog icon",
                        alignment = Alignment.Center,
                        contentScale = ContentScale.Fit
                    )

                    Spacer(Modifier.size(dimensionResource(id = R.dimen.margin_medium)))

                    Text(
                        text = title ?: stringResource(id = R.string.deeplink_edit_title),
                        style = MaterialTheme.typography.headlineSmall,
                    )

                    Spacer(Modifier.size(dimensionResource(id = R.dimen.margin_medium)))

                    OutlinedTextField(
                        value = deeplinkValue,
                        placeholder = {
                            Text(
                                text = deeplinkValue
                            )
                        },
                        label = { Text(text = stringResource(id = R.string.deeplink)) },
                        onValueChange = {
                            deeplinkValue = it
                        },
                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius))
                    )

                    Spacer(Modifier.size(dimensionResource(id = R.dimen.margin_medium)))

                    OutlinedTextField(
                        value = labelValue,
                        placeholder = {
                            Text(
                                text = labelValue
                            )
                        },
                        label = { Text(text = stringResource(id = R.string.deeplink_name)) },
                        onValueChange = { labelValue = it },
                        shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius))
                    )

                    Spacer(Modifier.size(dimensionResource(id = R.dimen.margin_medium)))

                    Row {
                        TextButton(onClick = onDismiss) {
                            Text(
                                text = stringResource(id = R.string.button_negative),
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }

                        /**
                         * On confirm, we edit the original deeplink with the new values. By doing
                         * this, we keep its id, which is used to identify it in the database.
                         */
                        TextButton(onClick = {
                            deeplink?.let {
                                it.apply {
                                    val map = deeplinkValue.buildDeeplinkObject(labelValue)
                                        .extractValuesFromDeeplink()
                                    label = (map[LABEL_KEY]) as String
                                    schema = (map[SCHEMA_KEY]) as String
                                    path = (map[PATH_KEY]) as String
                                    isInternal = (map[IS_INTERNAL_KEY]) as Boolean
                                }
                            }?.let {
                                labelValue = ""
                                deeplinkValue = ""
                                onConfirm(
                                    it

                                )
                            }
                        }) {
                            Text(
                                text = stringResource(id = R.string.edit),
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }
        }
    }
}
