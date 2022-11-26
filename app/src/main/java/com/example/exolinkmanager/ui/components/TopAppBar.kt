package com.example.exolinkmanager.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.example.exolinkmanager.R
import com.example.exolinkmanager.ui.models.Deeplink


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    selectedItem: String,
    itemList: List<Deeplink>,
    onMenuClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
    )
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        (stringResource(id = R.string.app_title) + " - " + selectedItem),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { onMenuClick.invoke() }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Menu"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "See Favorites"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        content = { innerPadding ->
            LazyColumn(
                contentPadding = PaddingValues(
                    horizontal = dimensionResource(id = R.dimen.margin_medium),
                    vertical = (dimensionResource(id = R.dimen.margin_medium) + innerPadding.calculateTopPadding())
                ),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.margin_medium))
            ) {
                itemList.forEach {
                    item {
                        ElevatedCard(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = dimensionResource(id = R.dimen.margin_medium))
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(
                                        horizontal = dimensionResource(id = R.dimen.margin_xlarge),
                                        vertical = dimensionResource(id = R.dimen.margin_large)
                                    )
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = it.label,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "Add to favorite !"
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}