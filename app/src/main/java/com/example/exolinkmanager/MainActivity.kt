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
import com.example.exolinkmanager.ui.models.Deeplink
import com.example.exolinkmanager.ui.models.Filters
import com.example.exolinkmanager.ui.theme.ExoLinkManagerTheme
import com.example.exolinkmanager.ui.viewmodels.CardsViewModel

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
                    SideMenu(MENU_LIST, DEEPLINK_LIST, cardsViewModel)
                }
            }
        }
    }

    companion object {
        val MENU_LIST = listOf(
            Filters.ALL,
            Filters.NEWEST,
            Filters.MOST_USED,
            Filters.OLDEST,
            Filters.RAREST,
            Filters.RECENT
        )

        val DEEPLINK_LIST = listOf(
            Deeplink(
                schema = "https://",
                host = "total",
                path = "electric-vehicle",
                internal = true,
                label = "Charge +"
            ),
            Deeplink(
                schema = "https://",
                host = "total",
                path = "eco-driving",
                internal = true,
                label = "Éco-driving"
            ),
            Deeplink(
                schema = "https://",
                host = "total",
                path = "patp",
                internal = true,
                label = "Pay-At-Pump"
            ),
            Deeplink(
                schema = "https://",
                host = "total",
                path = "faq",
                internal = true,
                label = "FAQ"
            ),
            Deeplink(
                schema = "https://",
                host = "total",
                path = "eco-driving",
                internal = true,
                label = "Éco-driving"
            ),
            Deeplink(
                schema = "https://",
                host = "total",
                path = "patp",
                internal = true,
                label = "Pay-At-Pump"
            ),
            Deeplink(
                schema = "https://",
                host = "total",
                path = "faq",
                internal = true,
                label = "FAQ"
            ),
            Deeplink(
                schema = "https://",
                host = "total",
                path = "eco-driving",
                internal = true,
                label = "Éco-driving"
            ),
            Deeplink(
                schema = "https://",
                host = "total",
                path = "patp",
                internal = true,
                label = "Pay-At-Pump"
            ),
            Deeplink(
                schema = "https://",
                host = "total",
                path = "faq",
                internal = true,
                label = "FAQ"
            ),
            Deeplink(
                schema = "https://",
                host = "total",
                path = "eco-driving",
                internal = true,
                label = "Éco-driving"
            ),
            Deeplink(
                schema = "https://",
                host = "total",
                path = "patp",
                internal = true,
                label = "Pay-At-Pump"
            ),
            Deeplink(
                schema = "https://",
                host = "total",
                path = "faq",
                internal = true,
                label = "FAQ"
            ),
            Deeplink(
                schema = "https://",
                host = "total",
                path = "eco-driving",
                internal = true,
                label = "Éco-driving"
            ),
            Deeplink(
                schema = "https://",
                host = "total",
                path = "patp",
                internal = true,
                label = "Pay-At-Pump"
            ),
            Deeplink(
                schema = "https://",
                host = "total",
                path = "faq",
                internal = true,
                label = "FAQ"
            ),
            Deeplink(
                schema = "https://",
                host = "total",
                path = "eco-driving",
                internal = true,
                label = "Éco-driving"
            ),
            Deeplink(
                schema = "https://",
                host = "total",
                path = "patp",
                internal = true,
                label = "Pay-At-Pump"
            ),
            Deeplink(
                schema = "https://",
                host = "total",
                path = "faq",
                internal = true,
                label = "FAQ"
            ),
            Deeplink(
                schema = "https://",
                host = "total",
                path = "eco-driving",
                internal = true,
                label = "Éco-driving"
            ),
            Deeplink(
                schema = "https://",
                host = "total",
                path = "patp",
                internal = true,
                label = "Pay-At-Pump"
            ),
            Deeplink(
                schema = "https://",
                host = "total",
                path = "faq",
                internal = true,
                label = "FAQ"
            ),
            Deeplink(
                schema = "https://",
                host = "total",
                path = "eco-driving",
                internal = true,
                label = "Éco-driving"
            ),
            Deeplink(
                schema = "https://",
                host = "total",
                path = "patp",
                internal = true,
                label = "Pay-At-Pump"
            ),
            Deeplink(
                schema = "https://",
                host = "total",
                path = "faq",
                internal = true,
                label = "FAQ"
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    SideMenu()
}