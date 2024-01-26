package com.example.exolinkmanager

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
                // TODO -> Replace surface with Scaffold and rework snackbar
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val scope = rememberCoroutineScope()
                    val snackbarHostState = remember {
                        SnackbarHostState()
                    }

                    Box {
                        MainScreen(MENU_LIST) {
                            try {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(it.buildFinalDeeplink())
                                    )
                                )
                            } catch (e: ActivityNotFoundException) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("No app found for deeplink ${it.label}")
                                }
                                Unit
                            }
                        }

                        SnackbarHost(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            hostState = snackbarHostState
                        )
                    }
                }
            }
        }
    }

    companion object {
        val MENU_LIST = listOf(
            Filters.ALL,
            Filters.RECENT,
            Filters.NEWEST,
            Filters.MOST_USED
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}