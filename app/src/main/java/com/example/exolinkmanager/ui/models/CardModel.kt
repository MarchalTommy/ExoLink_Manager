package com.example.exolinkmanager.ui.models

import androidx.compose.runtime.Immutable

@Immutable
data class CardModel(
    val id: Int,
    val title: String,
    val deeplink: Deeplink
)