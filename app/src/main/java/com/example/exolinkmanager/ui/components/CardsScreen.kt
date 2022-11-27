package com.example.exolinkmanager.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.exolinkmanager.ui.models.CardModel
import com.example.exolinkmanager.ui.viewmodels.CardsViewModel
import com.example.exolinkmanager.utils.dp
import kotlinx.coroutines.ExperimentalCoroutinesApi

// TODO: Manage favorite state of the items
// TODO: Manage delete / edit
@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalCoroutinesApi
@Composable
fun CardsScreen(
    parentInnerPadding: PaddingValues,
    viewModel: CardsViewModel
) {
    val cards by viewModel.cards.collectAsState()
    val revealedCardIds by viewModel.revealedCardIdsList.collectAsState()

    Scaffold(
        containerColor = Color.White,
        modifier = Modifier.padding(parentInnerPadding)
    ) { innerPadding ->
        LazyColumn(Modifier.padding(innerPadding)) {
            items(cards, CardModel::id) { card ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = androidx.compose.ui.Alignment.CenterStart,
                ) {
                    ActionRow(
                        onDelete = { /*TODO*/ },
                        onEdit = { /*TODO*/ },
                        onFavorite = { /*TODO*/ },
                        isFavorite = false,
                        iconSize = 56.dp,
                    )
                    DraggableCard(
                        card = card,
                        cardHeight = 56.dp,
                        isRevealed = revealedCardIds.contains(card.id),
                        cardOffset = (168f).dp(),
                        onExpand = { viewModel.onCardRevealed(cardId = card.id) },
                        onCollapse = { viewModel.onCardHidden(cardId = card.id) })
                }
            }
        }

    }
}