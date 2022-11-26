package com.example.exolinkmanager.ui.models

data class Deeplink(
    /**
    https://
     */
    val schema: String = "https://",
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
        "${schema}internal$host$path"
    } else {
        "$schema$host$path"
    }
}
