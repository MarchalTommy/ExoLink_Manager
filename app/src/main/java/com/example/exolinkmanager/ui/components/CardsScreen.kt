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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.exolinkmanager.ui.models.CardModel
import com.example.exolinkmanager.ui.models.Deeplink
import com.example.exolinkmanager.ui.models.Filters
import com.example.exolinkmanager.ui.models.extractFromSchema
import com.example.exolinkmanager.ui.viewmodel.CardsViewModel
import com.example.exolinkmanager.utils.dp
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalCoroutinesApi
@Composable
fun CardsScreen(
    parentInnerPadding: PaddingValues,
    cardViewModel: CardsViewModel=viewModel(),
    onCardClick: ((Deeplink) -> Unit)
) {
    val cards by cardViewModel.cards.collectAsState()
    val revealedCardIds by cardViewModel.revealedCardIdsList.collectAsState()
    val selectedCardId by cardViewModel.selectedCardId.collectAsState()
    val favoritesCardsId by cardViewModel.favoritesDeeplinkList.collectAsState()
    val sortingState by cardViewModel.activeSort.collectAsState()

    val showDeleteDialog=rememberSaveable { mutableStateOf(false) }
    val showEditDialog=rememberSaveable { mutableStateOf(false) }

    cardViewModel.getFavoritesDeeplink()

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(parentInnerPadding)
    ) {

        item {
            QuickLinkLaunchBar(onCardClick)
        }

        val headerList=if (sortingState == Filters.ALL) {
            cards.map { it.deeplink.schema }.toSet()
        } else {
            setOf("")
        }

        headerList.forEach { schema ->

            val filteredCards=if (sortingState == Filters.ALL) {
                cards.filter { it.deeplink.schema == schema }
            } else {
                cards
            }

            if (sortingState == Filters.ALL) {
                stickyHeader {
                    ListHeader(
                        text=schema.extractFromSchema()
                    )
                }
            }

            items(
                items=filteredCards,
                key=CardModel::id
            ) { card ->
                Box(
                    modifier=Modifier
                        .fillMaxWidth()
                        .animateItemPlacement(),
                    contentAlignment=Alignment.CenterStart,
                ) {
                    ActionRow(
                        onDelete={
                            cardViewModel.setSelectedCardId(card.id)
                            showDeleteDialog.value=true
                        },
                        onEdit={
                            cardViewModel.setSelectedCardId(card.id)
                            showEditDialog.value=true
                        },
                        onFavorite={
                            cardViewModel.setSelectedCardId(card.id)
                            cardViewModel.setFavoriteState(card.deeplink)
                        },
                        isFavorite=favoritesCardsId.contains(card.id),
                        iconSize=56.dp,
                    )
                    DraggableCard(card=card,
                        cardHeight=56.dp,
                        isRevealed=revealedCardIds.contains(card.id),
                        cardOffset=(168f).dp(),
                        onExpand={ cardViewModel.onCardRevealed(cardId=card.id) },
                        onCollapse={ cardViewModel.onCardHidden(cardId=card.id) },
                        onClick={
                            onCardClick.invoke(card.deeplink)
                            cardViewModel.updateDeeplinkUsedData(
                                card,
                                cards
                            )
                        })
                }
            }
        }
    }

    cards.find { it.id == selectedCardId }?.deeplink?.let { deeplink ->
        EditDeeplinkCustomDialog(deeplink=deeplink,
            showDialog=showEditDialog.value,
            onConfirm={ modifiedDeeplink ->
                cardViewModel.editDeeplink(modifiedDeeplink)
                showEditDialog.value=false
            },
            onDismiss={ showEditDialog.value=false })
    }

    ConfirmationAlertDialog(
        showDialog=showDeleteDialog.value,
        onConfirm={
            cardViewModel.removeDeeplink(selectedCardId)
            showDeleteDialog.value=false
        },
        onDismiss={ showDeleteDialog.value=false },
        title="Delete deeplink",
        message="Are you sure you want to delete this deeplink?",
        icon=Icons.Filled.Warning
    )
}