package com.example.exolinkmanager.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exolinkmanager.ui.models.CardModel
import com.example.exolinkmanager.ui.models.Deeplink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CardsViewModel : ViewModel() {

    private val _cards = MutableStateFlow(listOf<CardModel>())
    val cards = _cards as StateFlow<List<CardModel>>

    private val _revealedCardIdsList = MutableStateFlow(listOf<Int>())
    val revealedCardIdsList = _revealedCardIdsList as StateFlow<List<Int>>

    init {
        populateList()
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
                            internal = true
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

}