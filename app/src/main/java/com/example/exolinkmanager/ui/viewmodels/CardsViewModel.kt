package com.example.exolinkmanager.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exolinkmanager.ui.models.CardModel
import com.example.exolinkmanager.ui.models.Deeplink
import com.google.firebase.firestore.FirebaseFirestore
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

    // TODO: Remove this to implements usecases and repository
    fun fetchDeeplinks(firestore: FirebaseFirestore) {
        viewModelScope.launch {
            firestore.collection("deeplinks")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val result = task.result

                        if (result != null) {
                            val list = result.toObjects(Deeplink::class.java)

                            viewModelScope.launch {
                                _cards.emit(list.map { deeplink ->
                                    CardModel(
                                        id = deeplink.hashCode(),
                                        title = deeplink.label,
                                        deeplink = deeplink
                                    )
                                })
                            }

                        }

                    } else {
                        task.exception?.let { exception ->
                            throw exception
                        }
                    }
                }
        }
    }

}