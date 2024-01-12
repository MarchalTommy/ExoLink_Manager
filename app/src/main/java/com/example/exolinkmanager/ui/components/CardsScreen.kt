package com.example.exolinkmanager.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.exolinkmanager.ui.models.CardModel
import com.example.exolinkmanager.ui.models.Deeplink
import com.example.exolinkmanager.ui.models.Filters
import com.example.exolinkmanager.ui.models.extractFromSchema
import com.example.exolinkmanager.ui.viewmodels.CardsViewModel
import com.example.exolinkmanager.utils.dp
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalCoroutinesApi
@Composable
fun CardsScreen(
    parentInnerPadding: PaddingValues,
    viewModel: CardsViewModel,
    onCardClick: ((Deeplink) -> Unit)
) {
    val cards by viewModel.cards.collectAsState()
    val revealedCardIds by viewModel.revealedCardIdsList.collectAsState()
    val selectedCardId by viewModel.selectedCardId.collectAsState()
    val favoritesCardsId by viewModel.favoritesDeeplinkList.collectAsState()
    val sortingState by viewModel.activeSort.collectAsState()

    val showDeleteDialog = remember { mutableStateOf(false) }
    val showEditDialog = remember { mutableStateOf(false) }

    viewModel.getFavoritesDeeplink()

    Scaffold(
        containerColor = Color.White,
        modifier = Modifier.padding(parentInnerPadding)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(Modifier.padding(innerPadding)) {

                if (sortingState == Filters.ALL) {
                    var filteredCards: List<CardModel>
                    val headerList = cards.map { it.deeplink.schema }.toSet()

                    stickyHeader {
                        QuickLinkLaunchBar(viewModel, onCardClick)
                    }

                    headerList.forEach { schema ->

                        filteredCards = cards.filter { it.deeplink.schema == schema }

                        stickyHeader {
                            ListHeader(
                                text = schema.extractFromSchema()
                            )
                        }

                        items(items = filteredCards, key = CardModel::id) { card ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateItemPlacement(),
                                contentAlignment = Alignment.CenterStart,
                            ) {
                                ActionRow(
                                    onDelete = {
                                        viewModel.setSelectedCardId(card.id)
                                        showDeleteDialog.value = true
                                    },
                                    onEdit = {
                                        viewModel.setSelectedCardId(card.id)
                                        showEditDialog.value = true
                                    },
                                    onFavorite = {
                                        viewModel.setSelectedCardId(card.id)
                                        viewModel.setFavoriteState(card.deeplink)
                                    },
                                    isFavorite = favoritesCardsId.contains(card.id),
                                    iconSize = 56.dp,
                                )
                                DraggableCard(
                                    card = card,
                                    cardHeight = 56.dp,
                                    isRevealed = revealedCardIds.contains(card.id),
                                    cardOffset = (168f).dp(),
                                    onExpand = { viewModel.onCardRevealed(cardId = card.id) },
                                    onCollapse = { viewModel.onCardHidden(cardId = card.id) },
                                    onClick = {
                                        onCardClick.invoke(card.deeplink)
                                        viewModel.updateDeeplinkUsedData(card, cards)
                                    }
                                )
                            }
                        }
                    }
                } else {
                    items(items = cards, key = CardModel::id) { card ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItemPlacement(),
                            contentAlignment = Alignment.CenterStart,
                        ) {
                            ActionRow(
                                onDelete = {
                                    viewModel.setSelectedCardId(card.id)
                                    showDeleteDialog.value = true
                                },
                                onEdit = {
                                    viewModel.setSelectedCardId(card.id)
                                    showEditDialog.value = true
                                },
                                onFavorite = {
                                    viewModel.setSelectedCardId(card.id)
                                    viewModel.setFavoriteState(card.deeplink)
                                },
                                isFavorite = favoritesCardsId.contains(card.id),
                                iconSize = 56.dp,
                            )
                            DraggableCard(
                                card = card,
                                cardHeight = 56.dp,
                                isRevealed = revealedCardIds.contains(card.id),
                                cardOffset = (168f).dp(),
                                onExpand = { viewModel.onCardRevealed(cardId = card.id) },
                                onCollapse = { viewModel.onCardHidden(cardId = card.id) },
                                onClick = {
                                    onCardClick.invoke(card.deeplink)
                                    viewModel.updateDeeplinkUsedData(card, cards)
                                }
                            )
                        }
                    }
                }
            }
        }

        cards.find { it.id == selectedCardId }?.deeplink?.let { deeplink ->
            EditDeeplinkCustomDialog(
                deeplink = deeplink,
                showDialog = showEditDialog.value,
                onConfirm = { modifiedDeeplink ->
                    viewModel.editDeeplink(modifiedDeeplink)
                    showEditDialog.value = false
                },
                onDismiss = { showEditDialog.value = false }
            )
        }

        ConfirmationAlertDialog(
            showDialog = showDeleteDialog.value,
            onConfirm = {
                viewModel.removeDeeplink(selectedCardId)
                showDeleteDialog.value = false
            },
            onDismiss = { showDeleteDialog.value = false },
            title = "Delete deeplink",
            message = "Are you sure you want to delete this deeplink?",
            icon = Icons.Filled.Warning
        )
    }
}
