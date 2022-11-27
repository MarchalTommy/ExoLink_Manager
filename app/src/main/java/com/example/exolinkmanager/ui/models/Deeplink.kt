package com.example.exolinkmanager.ui.models

// TODO: Make it able to parse the firestore object
data class Deeplink(
    /**
    URL schema
     */
    val schema: String = "total://",
    /**
    deeplink host name
    ex: total
     */
    val host: String = "total",
    /**
    deeplink final element
    ex: /eco-driving
     */
    val path: String,
    /**
    Is an internal deeplink or not
     */
    val internal: Boolean,

    val label: String = "Deeplink"
)

fun Deeplink.buildFinalDeeplink(): String {
    return if (internal) {
        "${schema}internal/$path"
    } else {
        "$schema$path"
    }
}
