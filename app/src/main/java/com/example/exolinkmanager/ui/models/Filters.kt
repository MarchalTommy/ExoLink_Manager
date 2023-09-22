package com.example.exolinkmanager.ui.models

import com.example.exolinkmanager.R

enum class Filters {
    ALL,
    NEWEST,
    RECENT,
    MOST_USED
}

fun Filters.getFilterName(): String {
    return when (this) {
        Filters.RECENT -> "Recently used"
        Filters.MOST_USED -> "Most used"
        Filters.ALL -> "All deeplink"
        Filters.NEWEST -> "Newest"
    }
}

fun Filters.getFilterIcon(): Int {
    return when (this) {
        Filters.RECENT -> R.drawable.ic_recent_24px
        Filters.MOST_USED -> R.drawable.ic_most_used_24px
        Filters.ALL -> R.drawable.ic_all_24px
        Filters.NEWEST -> R.drawable.ic_newest_24px
    }
}