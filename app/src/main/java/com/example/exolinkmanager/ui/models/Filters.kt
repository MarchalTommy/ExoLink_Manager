package com.example.exolinkmanager.ui.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class Filters {
    RECENT,
    MOST_USED,
    ALL,
    RAREST,
    NEWEST,
    OLDEST
}

fun Filters.getFilterName(): String {
    return when (this) {
        Filters.RECENT -> "Recently used"
        Filters.MOST_USED -> "Most used"
        Filters.ALL -> "All deeplink"
        Filters.RAREST -> "Rarest"
        Filters.NEWEST -> "Newest"
        Filters.OLDEST -> "Oldest"
    }
}

fun Filters.getFilterIcon(): ImageVector {
    return when (this) {
        Filters.RECENT -> Icons.Filled.Settings
        Filters.MOST_USED -> Icons.Filled.Home
        Filters.ALL -> Icons.Filled.List
        Filters.RAREST -> Icons.Filled.Star
        Filters.NEWEST -> Icons.Filled.Refresh
        Filters.OLDEST -> Icons.Filled.ThumbUp
    }
}