package com.example.exolinkmanager.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.exolinkmanager.R
import com.example.exolinkmanager.ui.models.Deeplink
import com.example.exolinkmanager.ui.models.buildDeeplinkObject
import com.example.exolinkmanager.ui.viewmodels.CardsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickLinkLaunchBar(
    viewModel: CardsViewModel,
    onClick: (Deeplink) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = dimensionResource(id = R.dimen.margin_large)),
        contentAlignment = Alignment.Center,
    ) {

        var text by rememberSaveable { mutableStateOf("") }
        var active by rememberSaveable { mutableStateOf(false) }
        val deeplinks by viewModel.deeplinks.collectAsState()

        DockedSearchBar(
            modifier = Modifier,
            query = text,
            onQueryChange = { text = it },
            onSearch = {
                active = false
                if (text.isNotBlank() && text.matches("(.*)://(.*)".toRegex())) {
                    text.buildDeeplinkObject("").let {
                        onClick.invoke(it)
                    }
                }
            },
            active = active,
            onActiveChange = { active = it },
            placeholder = { Text(stringResource(id = R.string.quicklink_placeholder)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        ) {
            LazyColumn(
                modifier = Modifier,
            ) {
                val items = deeplinks.filter {
                    it.schema.contains(
                        text,
                        true
                    ) || it.label.contains(text, true) || it.path.contains(
                        text,
                        true
                    )
                }
                items(items = items) { item ->
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onClick.invoke(item)
                                active = false
                            }
                            .padding(
                                horizontal = dimensionResource(id = R.dimen.margin_large),
                                vertical = dimensionResource(
                                    id = R.dimen.margin_small
                                )
                            ),
                        text = item.label,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                    )
                    if (items.size - 1 != (items.indexOf(item))) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.margin_xxlarge)),
                            thickness = dimensionResource(id = R.dimen.divider_thickness)
                        )
                    }
                }
            }
        }

    }
}
