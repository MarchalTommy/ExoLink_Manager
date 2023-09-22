package com.example.exolinkmanager.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exolinkmanager.domain.usecase.AddDeeplinkUseCase
import com.example.exolinkmanager.domain.usecase.EditDeeplinkUseCase
import com.example.exolinkmanager.domain.usecase.FetchDeeplinksUseCase
import com.example.exolinkmanager.domain.usecase.GetFavoritesDeeplinkUseCase
import com.example.exolinkmanager.domain.usecase.RemoveDeeplinkUseCase
import com.example.exolinkmanager.domain.usecase.SetFavoriteStateUseCase
import com.example.exolinkmanager.ui.models.CardModel
import com.example.exolinkmanager.ui.models.Deeplink
import com.example.exolinkmanager.ui.models.buildDeeplinkObject
import com.example.exolinkmanager.ui.models.buildFinalDeeplink
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardsViewModel @Inject constructor(
    private val fetchDeeplinksUseCase: FetchDeeplinksUseCase,
    private val addDeeplinkUseCase: AddDeeplinkUseCase,
    private val removeDeeplinkUseCase: RemoveDeeplinkUseCase,
    private val editDeeplinkUseCase: EditDeeplinkUseCase,
    private val setFavoriteStateUseCase: SetFavoriteStateUseCase,
    private val getFavoritesDeeplinkUseCase: GetFavoritesDeeplinkUseCase
) : ViewModel() {

    private val _cards = MutableStateFlow(listOf<CardModel>())
    val cards = _cards as StateFlow<List<CardModel>>

    private val _revealedCardIdsList = MutableStateFlow(listOf<String>())
    val revealedCardIdsList = _revealedCardIdsList as StateFlow<List<String>>

    private val _selectedCardId = MutableStateFlow("")
    val selectedCardId = _selectedCardId as StateFlow<String>

    private val _actualDeeplinkChosen = MutableStateFlow("")
    val actualDeeplinkChosen = _actualDeeplinkChosen as StateFlow<String>

    private val _favoritesDeeplinkList = MutableStateFlow(listOf<String>())
    val favoritesDeeplinkList = _favoritesDeeplinkList.asStateFlow()

    private val _isFavoriteOnlyView = MutableStateFlow(false)
    val isFavoriteOnlyView = _isFavoriteOnlyView.asStateFlow()

    private fun inverseIsFavoriteOnlyView() {
        viewModelScope.launch {
            _isFavoriteOnlyView.emit(!_isFavoriteOnlyView.value)
            if (_isFavoriteOnlyView.value) {
                _cards.emit(_cards.value.filter { card ->
                    _favoritesDeeplinkList.value.contains(card.id)
                })
            } else {
                fetchDeeplinks()
            }
        }
    }

    init {
        fetchDeeplinks()
        getFavoritesDeeplink()
    }

    private fun setFavoritesDeeplinkList(favoriteList: List<String>) {
        viewModelScope.launch {
            _favoritesDeeplinkList.emit(favoriteList)
        }
    }

    fun setSelectedCardId(id: String) {
        viewModelScope.launch {
            _selectedCardId.emit(id)
        }
    }

    fun onCardRevealed(cardId: String) {
        if (_revealedCardIdsList.value.contains(cardId)) return
        _revealedCardIdsList.value = _revealedCardIdsList.value.toMutableList().also { list ->
            list.add(cardId)
        }
    }

    fun onCardHidden(cardId: String) {
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
                                id = deeplink.id ?: "",
                                title = deeplink.label,
                                deeplink = deeplink
                            )
                        })
                    }
                }
            }
        }
    }

    fun onCardClick(cardId: String) {
        viewModelScope.launch {
            _actualDeeplinkChosen.emit(_cards.value.first { it.id == cardId }.deeplink.buildFinalDeeplink())
        }
    }

    fun onFabClick(deeplink: String, label: String, success: (Boolean) -> Unit) {
        viewModelScope.launch {
            addDeeplinkUseCase.invoke(
                deeplink.buildDeeplinkObject(label)
            ) {
                if (it) {
                    fetchDeeplinks()
                }
                success.invoke(it)
            }
        }
    }

    fun removeDeeplink(selectedCardId: String) {
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

    fun editDeeplink(deeplink: Deeplink) {
        viewModelScope.launch {
            editDeeplinkUseCase.invoke(deeplink) {
                if (it) {
                    _cards.value = _cards.value.toMutableList().also { list ->
                        list.first { card -> card.id == deeplink.id }.deeplink = deeplink
                    }
                }
            }
        }
    }

    fun setFavoriteState(deeplink: Deeplink) {
        viewModelScope.launch {
            setFavoriteStateUseCase.invoke(deeplink)
            getFavoritesDeeplink()
        }
    }

    fun getFavoritesDeeplink() {
        viewModelScope.launch {
            getFavoritesDeeplinkUseCase.invoke().collect {
                setFavoritesDeeplinkList(it)
            }
        }
    }

    fun onFavoriteOnlyClick() {
        viewModelScope.launch {
            inverseIsFavoriteOnlyView()
        }
    }

}