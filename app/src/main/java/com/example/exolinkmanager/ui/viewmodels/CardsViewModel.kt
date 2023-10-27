package com.example.exolinkmanager.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exolinkmanager.domain.model.toDeeplink
import com.example.exolinkmanager.domain.usecase.AddDeeplinkUseCase
import com.example.exolinkmanager.domain.usecase.EditDeeplinkUseCase
import com.example.exolinkmanager.domain.usecase.FetchDeeplinksUseCase
import com.example.exolinkmanager.domain.usecase.GetDeeplinkByUseUseCase
import com.example.exolinkmanager.domain.usecase.GetFavoritesDeeplinkUseCase
import com.example.exolinkmanager.domain.usecase.GetLastUsedDeeplinksIdsUseCase
import com.example.exolinkmanager.domain.usecase.IncrementDeeplinkUseUseCase
import com.example.exolinkmanager.domain.usecase.RemoveDeeplinkUseCase
import com.example.exolinkmanager.domain.usecase.SaveDeeplinksLastUsedDateUseCase
import com.example.exolinkmanager.domain.usecase.SetFavoriteStateUseCase
import com.example.exolinkmanager.ui.models.CardModel
import com.example.exolinkmanager.ui.models.Deeplink
import com.example.exolinkmanager.ui.models.Filters
import com.example.exolinkmanager.ui.models.buildDeeplinkObject
import com.example.exolinkmanager.ui.models.getFilterName
import com.example.exolinkmanager.ui.models.toBusinessDeeplink
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardsViewModel @Inject constructor(
    private val fetchDeeplinksUseCase: FetchDeeplinksUseCase,
    private val addDeeplinkUseCase: AddDeeplinkUseCase,
    private val removeDeeplinkUseCase: RemoveDeeplinkUseCase,
    private val editDeeplinkUseCase: EditDeeplinkUseCase,
    private val setFavoriteStateUseCase: SetFavoriteStateUseCase,
    private val getFavoritesDeeplinkUseCase: GetFavoritesDeeplinkUseCase,
    private val saveDeeplinksLastUsedDateUseCase: SaveDeeplinksLastUsedDateUseCase,
    private val getLastUsedDeeplinksIdsUseCase: GetLastUsedDeeplinksIdsUseCase,
    private val incrementDeeplinkUseUseCase: IncrementDeeplinkUseUseCase,
    private val getDeeplinkByUseUseCase: GetDeeplinkByUseUseCase
) : ViewModel() {

    private val _cards = MutableStateFlow(listOf<CardModel>())
    val cards = _cards as StateFlow<List<CardModel>>

    private val _revealedCardIdsList = MutableStateFlow(listOf<String>())
    val revealedCardIdsList = _revealedCardIdsList as StateFlow<List<String>>

    private val _selectedCardId = MutableStateFlow("")
    val selectedCardId = _selectedCardId as StateFlow<String>

    private val _favoritesDeeplinkList = MutableStateFlow(listOf<String>())
    val favoritesDeeplinkList = _favoritesDeeplinkList.asStateFlow()

    private val _isFavoriteOnlyView = MutableStateFlow(false)
    val isFavoriteOnlyView = _isFavoriteOnlyView.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _isInError = MutableStateFlow(false)
    val isInError = _isInError.asStateFlow()

    private val _activeSort = MutableStateFlow(Filters.ALL)
    private val activeSort = _activeSort.asStateFlow()

    private fun setIsInError(error: Boolean) {
        viewModelScope.launch {
            _isInError.emit(error)
        }
    }

    private fun setIsLoading(loading: Boolean) {
        viewModelScope.launch {
            _isLoading.emit(loading)
        }
    }

    private fun inverseIsFavoriteOnlyView() {
        setIsLoading(true)
        viewModelScope.launch {
            _isFavoriteOnlyView.emit(!_isFavoriteOnlyView.value)
            if (_isFavoriteOnlyView.value) {
                setIsLoading(false)
                _cards.emit(_cards.value.filter { card ->
                    _favoritesDeeplinkList.value.contains(card.id)
                })
            } else {
                fetchDeeplinks()
            }
        }
    }

    init {
        setIsInError(false)
        setIsLoading(true)
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
                setIsLoading(false)
                if (it != null) {
                    setIsInError(false)
                    viewModelScope.launch {
                        _cards.emit(it.map { businessDeeplink ->
                            CardModel(
                                id = businessDeeplink.id ?: "",
                                title = businessDeeplink.label,
                                deeplink = businessDeeplink.toDeeplink()
                            )
                        })
                    }
                } else {
                    setIsInError(true)
                }
            }
        }
    }

    fun onFabClick(deeplink: String, label: String, success: (Boolean) -> Unit) {
        viewModelScope.launch {
            addDeeplinkUseCase.invoke(
                deeplink.buildDeeplinkObject(label).toBusinessDeeplink()
            ) {
                if (it) {
                    setIsInError(false)
                    fetchDeeplinks()
                } else {
                    setIsInError(true)
                }
                success.invoke(it)
            }
        }
    }

    fun removeDeeplink(selectedCardId: String) {
        _cards.value.first { it.id == selectedCardId }.let { card ->
            viewModelScope.launch {
                removeDeeplinkUseCase.invoke(card.deeplink.toBusinessDeeplink()) {
                    if (it) {
                        setIsInError(false)
                        _cards.value = _cards.value.toMutableList().also { list ->
                            list.remove(card)
                        }
                    } else {
                        setIsInError(true)
                    }
                }
            }
        }
    }

    fun editDeeplink(deeplink: Deeplink) {
        viewModelScope.launch {
            editDeeplinkUseCase.invoke(deeplink.toBusinessDeeplink()) {
                if (it) {
                    setIsInError(false)
                    _cards.value = _cards.value.toMutableList().also { list ->
                        list.first { card -> card.id == deeplink.id }.deeplink = deeplink
                    }
                } else {
                    setIsInError(true)
                }
            }
        }
    }

    fun setFavoriteState(deeplink: Deeplink) {
        viewModelScope.launch {
            setFavoriteStateUseCase.invoke(deeplink.toBusinessDeeplink())
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

    fun updateDeeplinkUsedData(selectedCard: CardModel, deeplinkList: List<CardModel>) {
        viewModelScope.launch {
            selectedCard.deeplink.lastTimeUsed = Timestamp.now()
            selectedCard.deeplink.id?.let { incrementDeeplinkUseUseCase.invoke(it) }
            deeplinkList.first { it.id == selectedCard.id }.deeplink = selectedCard.deeplink
            saveDeeplinksLastUsedDateUseCase.invoke(deeplinkList.map { it.deeplink })
            if (activeSort.value == Filters.RECENT) {
                getLastUsedDeeplink()
            }

            // TODO: save number of time used
        }
    }

    private fun getLastUsedDeeplink() {
        viewModelScope.launch {
            _activeSort.emit(Filters.RECENT)
            getLastUsedDeeplinksIdsUseCase.invoke().collect { idMap ->
                applyFilter(idMap)
            }
        }
    }

    private fun getDeeplinkListOrderedByUse() {
        viewModelScope.launch {
            _activeSort.emit(Filters.MOST_USED)
            getDeeplinkByUseUseCase.invoke().collect { idMap ->
                applyFilter(idMap)
            }
        }
    }

    private suspend fun applyFilter(idMap: Map<String, Int>) {
        fetchDeeplinksUseCase.invoke { deeplinkList ->
            deeplinkList?.map { businessDeeplink ->
                CardModel(
                    id = businessDeeplink.id ?: "",
                    title = businessDeeplink.label,
                    deeplink = businessDeeplink.toDeeplink()
                )
            }?.let { cards ->
                when(activeSort.value) {
                    Filters.RECENT -> {
                        _cards.tryEmit(cards.sortedBy { card ->
                            idMap[card.deeplink.id]
                        })
                    }
                    Filters.MOST_USED -> {
                        _cards.tryEmit(cards.sortedByDescending { card ->
                            idMap[card.deeplink.id]
                        })
                    }
                    else -> {
                        cards.sortedBy { card ->
                            idMap[card.deeplink.id]
                        }
                    }
                }

            }
        }
    }

    fun filterDeeplinks(filter: String) {
        viewModelScope.launch {
            val filteredList = mutableListOf<CardModel>()
            when (filter) {
                Filters.RECENT.getFilterName() -> {
                    getLastUsedDeeplink()
                }

                Filters.MOST_USED.getFilterName() -> {
                    getDeeplinkListOrderedByUse()
                }

                Filters.NEWEST.getFilterName() -> {

                }

                Filters.ALL.getFilterName() -> {
                    fetchDeeplinks()
//                    cards.value.sortedBy { TODO("Trier by header / project") }
                }

            }

            _cards.emit(
                filteredList
            )
        }
    }

}