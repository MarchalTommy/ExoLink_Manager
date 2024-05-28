package com.example.exolinkmanager

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.exolinkmanager.ui.components.MainScreen
import com.example.exolinkmanager.ui.models.Filters
import com.example.exolinkmanager.ui.models.buildFinalDeeplink
import com.example.exolinkmanager.ui.theme.ExoLinkManagerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExoLinkManagerTheme {
                val showNewDeeplinkDialog=remember { mutableStateOf(false) }
                val scope=rememberCoroutineScope()
                val snackBarHostState=remember {
                    SnackbarHostState()
                }
                Scaffold(
                    modifier=Modifier
                        .fillMaxSize(),
                    snackbarHost={
                        SnackbarHost(hostState=snackBarHostState,
                            snackbar={
                                Snackbar(
                                    modifier=Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        horizontalArrangement=Arrangement.SpaceBetween,
                                        verticalAlignment=Alignment.CenterVertically,
                                        modifier=Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text=it.visuals.message
                                        )
                                    }
                                }
                            })
                    },
                    floatingActionButton={
                        FloatingActionButton(containerColor=MaterialTheme.colorScheme.tertiary,
                            onClick={
                                showNewDeeplinkDialog.value=true
                            }) {
                            Icon(
                                imageVector=Icons.Filled.Add,
                                contentDescription="Add a new deeplink"
                            )
                        }
                    },
                    floatingActionButtonPosition=FabPosition.End
                ) { paddingValues ->
                    MainScreen(
                        paddingValues,
                        MENU_LIST,
                        showNewDeeplinkDialog,
                        snackBarHostState
                    ) {
                        try {
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(it.buildFinalDeeplink())
                                )
                            )
                        } catch (e: ActivityNotFoundException) {
                            scope.launch {
                                snackBarHostState.showSnackbar("No app found for deeplink ${it.label}")
                            }
                            Unit
                        }
                    }
                }
            }
        }
    }

    companion object {
        val MENU_LIST=listOf(
            Filters.ALL,
            Filters.RECENT,
            Filters.NEWEST,
            Filters.MOST_USED
        )
    }
}