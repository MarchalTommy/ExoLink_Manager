package com.example.exolinkmanager.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exolinkmanager.domain.usecase.AddDeeplinkUseCase
import com.example.exolinkmanager.domain.usecase.FetchDeeplinksUseCase
import com.example.exolinkmanager.domain.usecase.RemoveDeeplinkUseCase
import com.example.exolinkmanager.ui.models.CardModel
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

    private val _onFabClick = MutableStateFlow(false)
    val onFabClick = _onFabClick as StateFlow<Boolean>

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
                                id = deeplink.hashCode(),
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

    fun onFabClick() {
        viewModelScope.launch {
            _onFabClick.emit(true)
        }
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