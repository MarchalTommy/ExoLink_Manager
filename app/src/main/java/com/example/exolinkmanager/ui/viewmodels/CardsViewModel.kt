package com.example.exolinkmanager.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exolinkmanager.domain.usecase.AddDeeplinkUseCase
import com.example.exolinkmanager.domain.usecase.FetchDeeplinksUseCase
import com.example.exolinkmanager.domain.usecase.RemoveDeeplinkUseCase
import com.example.exolinkmanager.ui.models.CardModel
import com.example.exolinkmanager.ui.models.Deeplink
import com.example.exolinkmanager.ui.models.buildFinalDeeplink
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardsViewModel @Inject constructor(
    private val fetchDeeplinksUseCase: FetchDeeplinksUseCase,
    private val addDeeplinkUseCase: AddDeeplinkUseCase,
    private val removeDeeplinkUseCase: RemoveDeeplinkUseCase
) : ViewModel() {

    private val _cards = MutableStateFlow(listOf<CardModel>())
    val cards = _cards as StateFlow<List<CardModel>>

    private val _revealedCardIdsList = MutableStateFlow(listOf<Int>())
    val revealedCardIdsList = _revealedCardIdsList as StateFlow<List<Int>>

    private val _selectedCardId = MutableStateFlow(0)
    val selectedCardId = _selectedCardId as StateFlow<Int>

    private val _actualDeeplinkChosen = MutableStateFlow("")
    val actualDeeplinkChosen = _actualDeeplinkChosen as StateFlow<String>

    init {
        fetchDeeplinks()
    }

    fun setSelectedCardId(id: Int) {
        viewModelScope.launch {
            _selectedCardId.emit(id)
        }
    }

    fun onCardRevealed(cardId: Int) {
        if (_revealedCardIdsList.value.contains(cardId)) return
        _revealedCardIdsList.value = _revealedCardIdsList.value.toMutableList().also { list ->
            list.add(cardId)
        }
    }

    fun onCardHidden(cardId: Int) {
        if (!_revealedCardIdsList.value.contains(cardId)) return
        _revealedCardIdsList.value = _revealedCardIdsList.value.toMutableList().also { list ->
            list.remove(cardId)
        }
    }

    private fun fetchDeeplinks() {
        viewModelScope.launch {
            fetchDeeplinksUseCase.invoke {
                if (it != null) {
                    viewModelScope.launch {
                        _cards.emit(it.map { deeplink ->
                            CardModel(
                                id = deeplink.label.hashCode(),
                                title = deeplink.label,
                                deeplink = deeplink
                            )
                        })
                    }
                }
            }
        }
    }

    fun onCardClick(cardId: Int) {
        viewModelScope.launch {
            _actualDeeplinkChosen.emit(_cards.value.first { it.id == cardId }.deeplink.buildFinalDeeplink())
        }
    }

    fun onFabClick(deeplink: String, success: (Boolean) -> Unit) {
        viewModelScope.launch {
            Log.d(
                "FAB CLICK",
                "Deeplink string --> $deeplink\nDeeplink object --> ${
                    generateDeeplinkObject(deeplink)
                }"
            )
            addDeeplinkUseCase.invoke(
                generateDeeplinkObject(deeplink)
            ) {
                if (it) {
                    fetchDeeplinks()
                }
                success.invoke(it)
            }
        }
    }

    private fun generateDeeplinkObject(deeplink: String): Deeplink {
        return Deeplink(
            schema = deeplink.split(":")[0] + "://",
            path = if (deeplink.contains("internal")) {
                deeplink.split("/")[3].split("|")[0]
            } else {
                deeplink.split("/")[2].split("|")[0]
            },
            isInternal = deeplink.contains("internal"),
            label = deeplink.split("|")[1]
        )
    }

    fun removeDeeplink(selectedCardId: Int) {
        _cards.value.first { it.id == selectedCardId }.let { card ->
            viewModelScope.launch {
                removeDeeplinkUseCase.invoke(card.deeplink) {
                    if (it) {
                        _cards.value = _cards.value.toMutableList().also { list ->
                            list.remove(card)
                        }
                    }
                }
            }
        }
    }

}