package com.example.exolinkmanager.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.exolinkmanager.ui.models.CardModel
import com.example.exolinkmanager.ui.viewmodels.CardsViewModel
import com.example.exolinkmanager.utils.dp
import kotlinx.coroutines.ExperimentalCoroutinesApi

// TODO: Manage favorite state of the items
@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalCoroutinesApi
@Composable
fun CardsScreen(
    parentInnerPadding: PaddingValues,
    viewModel: CardsViewModel
) {
    val cards by viewModel.cards.collectAsState()
    val revealedCardIds by viewModel.revealedCardIdsList.collectAsState()
    val selectedCardId by viewModel.selectedCardId.collectAsState()

    val showDeleteDialog = remember { mutableStateOf(false) }
    val showEditDialog = remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.White,
        modifier = Modifier.padding(parentInnerPadding)
    ) { innerPadding ->
        LazyColumn(Modifier.padding(innerPadding)) {
            items(items = cards, key = CardModel::id) { card ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = androidx.compose.ui.Alignment.CenterStart,
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
                        onFavorite = { /*TODO*/
                            viewModel.setSelectedCardId(card.id)
                        },
                        isFavorite = false,
                        iconSize = 56.dp,
                    )
                    DraggableCard(
                        card = card,
                        cardHeight = 56.dp,
                        isRevealed = revealedCardIds.contains(card.id),
                        cardOffset = (168f).dp(),
                        onExpand = { viewModel.onCardRevealed(cardId = card.id) },
                        onCollapse = { viewModel.onCardHidden(cardId = card.id) },
                        onClick = { viewModel.onCardClick(cardId = card.id) }
                    )
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
