package com.example.exolinkmanager.ui.models

data class Deeplink(

    val id: String? = null,

    /**
     * Start of the deeplink,
     * The application it is linked to
     */
    var schema: String = "total://",

    /**
    deeplink final element
    ex: eco-driving
     */
    var path: String = "",

    /**
    Is an internal deeplink or not
     */
    var isInternal: Boolean = false,

    /**
     * Showing name of the deeplink. Put something easy to understand here
     */
    var label: String = "Deeplink"
) {

}

fun Deeplink?.buildFinalDeeplink(): String {
    if (this == null) return ""
    return if (isInternal) {
        "${schema}internal/$path"
    } else {
        "$schema$path"
    }
}

fun String.buildDeeplinkObject(label: String?): Deeplink {
    return Deeplink(
        schema = this.split(":")[0] + "://",
        path = if (this.contains("internal")) {
            this.split("/")[3].split("|")[0]
        } else {
            this.split("/")[2].split("|")[0]
        },
        isInternal = this.contains("internal"),
        label = label ?: ""
    )
}

fun Deeplink.extractValuesFromDeeplink(): Map<String, Any> {
    val map = mutableMapOf<String, Any>()
    map[SCHEMA_KEY] = schema
    map[PATH_KEY] = path
    map[IS_INTERNAL_KEY] = isInternal
    map[LABEL_KEY] = label
    return map
}

const val SCHEMA_KEY = "schema"
const val PATH_KEY = "path"
const val IS_INTERNAL_KEY = "isInternal"
const val LABEL_KEY = "label"
