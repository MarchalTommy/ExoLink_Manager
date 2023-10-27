package com.example.exolinkmanager.data.local.model

import com.example.exolinkmanager.domain.model.BusinessDeeplink
import com.example.exolinkmanager.ui.models.Deeplink
import com.google.firebase.Timestamp

data class LocalDeeplink(

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
    var label: String = "Deeplink",

    /**
     * Last time the deeplink was used. Used only for sorting
     */
    var lastTimeUsed: Timestamp? = null,

    /**
     * Date of creation of the deeplink, used for sorting
     */
    var creationDate: Timestamp = Timestamp.now()
)

fun LocalDeeplink.toBusinessDeeplink(): BusinessDeeplink {
    return BusinessDeeplink(
        id = id,
        schema = schema,
        path = path,
        isInternal = isInternal,
        label = label,
        lastTimeUsed = lastTimeUsed,
        creationDate = creationDate
    )
}