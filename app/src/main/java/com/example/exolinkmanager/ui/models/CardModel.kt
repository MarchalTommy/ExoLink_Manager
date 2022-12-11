package com.example.exolinkmanager.ui.models

import androidx.compose.runtime.Immutable

@Immutable
data class CardModel(
    val id: String,
    val title: String,
    var deeplink: Deeplink
)