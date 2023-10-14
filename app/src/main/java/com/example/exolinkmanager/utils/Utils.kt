package com.example.exolinkmanager.utils

import android.content.res.Resources


fun Float.dp(): Float = this * density + 0.5f

val density: Float
    get() = Resources.getSystem().displayMetrics.density

fun sortBy() {
    val things = setOf(Thing(2, "x"), Thing(1, "y"), Thing(3, "z"), Thing(4, "a"))
    val desiredOrder = listOf(3, 4, 1, 2).indexMap()
    val sortedThings = things.sortedBy { desiredOrder[it.id] }
    println(sortedThings)
    // prints: [Thing(id=3, name=z), Thing(id=4, name=a), Thing(id=1, name=y), Thing(id=2, name=x)]
}

data class Thing(val id: Int, val name: String)

fun <T> Iterable<T>.indexMap(): Map<T, Int> {
    val map = mutableMapOf<T, Int>()
    forEachIndexed { i, v ->
        map[v] = i
    }
    return map
}