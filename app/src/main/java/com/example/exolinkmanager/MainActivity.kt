package com.example.exolinkmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.exolinkmanager.ui.components.SideMenu
import com.example.exolinkmanager.ui.models.Filters
import com.example.exolinkmanager.ui.theme.ExoLinkManagerTheme
import com.example.exolinkmanager.ui.viewmodels.CardsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val cardsViewModel by viewModels<CardsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExoLinkManagerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SideMenu(MENU_LIST, cardsViewModel)
                }
            }
        }
    }

    companion object {
        val MENU_LIST = listOf(
            Filters.ALL,
            Filters.RECENT,
            Filters.NEWEST,
            Filters.OLDEST,
            Filters.MOST_USED,
            Filters.RAREST
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    SideMenu()
}