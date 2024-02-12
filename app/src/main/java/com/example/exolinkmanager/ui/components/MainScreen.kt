package com.example.exolinkmanager.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.exolinkmanager.R
import com.example.exolinkmanager.ui.models.Deeplink
import com.example.exolinkmanager.ui.models.Filters
import com.example.exolinkmanager.ui.models.getFilterIcon
import com.example.exolinkmanager.ui.models.getFilterName
import com.example.exolinkmanager.ui.viewmodel.CardsViewModel
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    paddingValues: PaddingValues,
    menuItems: List<Filters>?=null,
    showNewDeeplinkDialog: MutableState<Boolean>,
    snackBarHostState: SnackbarHostState,
    cardsViewModel: CardsViewModel=viewModel(),
    onCardClick: (Deeplink) -> Unit?={}
) {
    val drawerState=rememberDrawerState(initialValue=DrawerValue.Closed)
    val scope=rememberCoroutineScope()

    val selectedItem=remember { mutableStateOf(menuItems?.get(0)) }
    val shouldShowSnack=remember { mutableStateOf(false) }
    val snackIsSuccess=remember { mutableStateOf(false) }

    LaunchedEffect(key1=shouldShowSnack.value,
        block={
            if (shouldShowSnack.value) {
                snackBarHostState.showSnackbar(
                    message=if (snackIsSuccess.value) {
                        "Deeplink added successfully"
                    } else {
                        "Deeplink could not be added. Check your internet connection or your deeplink and try again."
                    },
                    withDismissAction=true,
                    duration=SnackbarDuration.Long
                )
                shouldShowSnack.value=false
            }
        })

    BackHandler(enabled=drawerState.isOpen) {
        scope.launch {
            drawerState.close()
        }
    }

    DismissibleNavigationDrawer(modifier=Modifier.padding(paddingValues),
        drawerState=drawerState,
        drawerContent={
            DismissibleDrawerSheet {
                Spacer(Modifier.height(dimensionResource(id=R.dimen.margin_medium)))
                menuItems?.forEach { item ->
                    NavigationDrawerItem(
                        label={ Text(item.getFilterName()) },
                        icon={
                            Icon(
                                painterResource(id=item.getFilterIcon()),
                                contentDescription=null
                            )
                        },
                        selected=item == selectedItem.value,
                        onClick={
                            cardsViewModel.filterDeeplinks(item.getFilterName())
                            selectedItem.value=item
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        modifier=Modifier.padding(horizontal=dimensionResource(id=R.dimen.margin_medium))
                    )
                }
            }
        },
        content={
            TopAppBar(selectedItem=selectedItem.value?.getFilterName() ?: "",
                onMenuClick={
                    scope.launch {
                        if (drawerState.isClosed) {
                            drawerState.open()
                        } else {
                            drawerState.close()
                        }
                    }
                },
                onFavoriteOnlyClick={
                    cardsViewModel.onFavoriteOnlyClick()
                },
                {
                    onCardClick.invoke(it)
                })

            val isLoading by cardsViewModel.isLoading.collectAsState()
            Loader(isLoading)

            NewDeeplinkCustomDialog(
                showDialog=showNewDeeplinkDialog.value,
                onConfirm={ deeplink, label ->
                    cardsViewModel.onFabClick(
                        deeplink,
                        label
                    ) { success ->
                        snackIsSuccess.value=success
                        shouldShowSnack.value=true
                    }
                    showNewDeeplinkDialog.value=false
                },
                onDismiss={ showNewDeeplinkDialog.value=false },
                value=""
            )
        })
}