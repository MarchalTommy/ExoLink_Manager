package com.example.exolinkmanager.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.exolinkmanager.R
import com.example.exolinkmanager.ui.models.Filters
import com.example.exolinkmanager.ui.models.getFilterIcon
import com.example.exolinkmanager.ui.models.getFilterName
import com.example.exolinkmanager.ui.viewmodels.CardsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SideMenu(
    menuItems: List<Filters>? = null,
    viewModel: CardsViewModel? = null
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val selectedItem = remember { mutableStateOf(menuItems?.get(0)) }

    BackHandler(enabled = drawerState.isOpen) {
        scope.launch {
            drawerState.close()
        }
    }

    DismissibleNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DismissibleDrawerSheet {
                Spacer(Modifier.height(dimensionResource(id = R.dimen.margin_medium)))
                menuItems?.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(item.getFilterName()) },
                        icon = {
                            if (selectedItem.value == item) {
                                Icon(Icons.Rounded.Check, contentDescription = null)
                            } else {
                                Icon(item.getFilterIcon(), contentDescription = null)
                            }
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
        },
        content = {
            if (viewModel != null) {
                TopAppBar(
                    selectedItem = selectedItem.value?.getFilterName() ?: "",
                    viewModel = viewModel
                ) {
                    scope.launch {
                        drawerState.open()
                    }
                }
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
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    onClick = { viewModel?.onFabClick() }
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add a new deeplink")
                }
            }
        }
    )
}