package com.example.exolinkmanager.ui.models

data class Deeplink(


    val schema: String = "total://",
    /**
    deeplink final element
    ex: /eco-driving
     */
    val path: String = "",
    /**
    Is an internal deeplink or not
     */
    val isInternal: Boolean = false,

    val label: String = "Deeplink"
) {

}

fun Deeplink.buildFinalDeeplink(): String {
    return if (isInternal) {
        "${schema}internal/$path"
    } else {
        "$schema$path"
    }
}
