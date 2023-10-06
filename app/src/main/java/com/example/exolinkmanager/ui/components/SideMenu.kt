package com.example.exolinkmanager.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import com.example.exolinkmanager.R
import com.example.exolinkmanager.ui.models.Filters
import com.example.exolinkmanager.ui.models.getFilterIcon
import com.example.exolinkmanager.ui.models.getFilterName
import com.example.exolinkmanager.ui.viewmodels.CardsViewModel
import kotlinx.coroutines.launch

@Composable
fun SideMenu(
    menuItems: List<Filters>? = null, viewModel: CardsViewModel? = null
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val selectedItem = remember { mutableStateOf(menuItems?.get(0)) }
    val showNewDeeplinkDialog = remember { mutableStateOf(false) }
    val showSuccessSnackBar = remember { mutableStateOf(false) }
    val showSnackBar = remember { mutableStateOf(false) }

    BackHandler(enabled = drawerState.isOpen) {
        scope.launch {
            drawerState.close()
        }
    }

    DismissibleNavigationDrawer(drawerState = drawerState, drawerContent = {
        DismissibleDrawerSheet {
            Spacer(Modifier.height(dimensionResource(id = R.dimen.margin_medium)))
            menuItems?.forEach { item ->
                NavigationDrawerItem(
                    label = { Text(item.getFilterName()) },
                    icon = {
                        Icon(
                            painterResource(id = item.getFilterIcon()),
                            contentDescription = null
                        )
                    },
                    selected = item == selectedItem.value,
                    onClick = {
                        selectedItem.value = item
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.margin_medium))
                )
            }
        }
    }, content = {
        if (viewModel != null) {
            TopAppBar(
                selectedItem = selectedItem.value?.getFilterName() ?: "", viewModel = viewModel,
                onMenuClick = {
                    scope.launch {
                        drawerState.open()
                    }
                },
                onFavoriteOnlyClick = {
                    viewModel.onFavoriteOnlyClick()
                }
            )

            val isLoading by viewModel.isLoading.collectAsState()
            Loader(isLoading)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.margin_xxxlarge)),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            FloatingActionButton(containerColor = MaterialTheme.colorScheme.secondaryContainer,
                onClick = {
                    showNewDeeplinkDialog.value = true
                }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add a new deeplink")
            }
        }

        NewDeeplinkCustomDialog(
            showDialog = showNewDeeplinkDialog.value,
            onConfirm = { deeplink, label ->
                viewModel?.onFabClick(deeplink, label) { success ->
                    showSnackBar.value = true
                    showSuccessSnackBar.value = success
                }
                showNewDeeplinkDialog.value = false
            },
            onDismiss = { showNewDeeplinkDialog.value = false },
            value = ""
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom
        ) {
            if (showSnackBar.value) {
                if (showSuccessSnackBar.value) {
                    Snackbar(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Deeplink added successfully")
                            IconButton(onClick = { showSnackBar.value = false }) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = "Dismiss"
                                )
                            }
                        }
                    }
                } else {
                    Snackbar(
                        modifier = Modifier.fillMaxWidth(),
                        dismissAction = {
                            IconButton(onClick = { showSnackBar.value = false }) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = "Dismiss"
                                )
                            }
                        },
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Deeplink could not be added. Check your internet connection and retry.")
                            IconButton(onClick = { showSnackBar.value = false }) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = "Dismiss"
                                )
                            }
                        }
                    }
                }
            }
        }
    })
}