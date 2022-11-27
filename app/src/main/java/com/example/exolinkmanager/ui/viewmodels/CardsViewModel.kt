package com.example.exolinkmanager.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exolinkmanager.domain.usecase.AddDeeplinkUseCase
import com.example.exolinkmanager.domain.usecase.FetchDeeplinksUseCase
import com.example.exolinkmanager.ui.models.CardModel
import com.example.exolinkmanager.ui.models.Deeplink
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardsViewModel @Inject constructor(
    val fetchDeeplinksUseCase: FetchDeeplinksUseCase,
    val addDeeplinkUseCase: AddDeeplinkUseCase
) : ViewModel() {

    private val _cards = MutableStateFlow(listOf<CardModel>())
    val cards = _cards as StateFlow<List<CardModel>>

    private val _revealedCardIdsList = MutableStateFlow(listOf<Int>())
    val revealedCardIdsList = _revealedCardIdsList as StateFlow<List<Int>>

    init {
        fetchDeeplinks()
//        populateList()
    }

    private fun populateList() {
        viewModelScope.launch {
            val mockList = arrayListOf<CardModel>()
            repeat(23) {
                mockList.add(
                    CardModel(
                        id = it,
                        title = "Card $it",
                        deeplink = Deeplink(
                            path = "/eco-driving",
                            isInternal = true
                        )
                    )
                )
            }
            _cards.emit(mockList)
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

    fun fetchDeeplinks() {
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

}