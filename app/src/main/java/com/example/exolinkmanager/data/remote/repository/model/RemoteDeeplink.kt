package com.example.exolinkmanager.data.remote.repository.model

import com.example.exolinkmanager.data.local.model.LocalDeeplink
import com.example.exolinkmanager.domain.model.BusinessDeeplink
import com.google.firebase.Timestamp

data class RemoteDeeplink(

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
     * Date of creation of the deeplink, used for sorting
     */
    var creationDate: Timestamp? = null
)

fun RemoteDeeplink.toLocalDeeplink(): LocalDeeplink {
    return LocalDeeplink(
        id = id,
        schema = schema,
        path = path,
        isInternal = isInternal,
        label = label,
        creationDate = creationDate,
        lastTimeUsed = null
    )
}

fun RemoteDeeplink.toBusinessDeeplink(): BusinessDeeplink {
    return BusinessDeeplink(
        id = id,
        schema = schema,
        path = path,
        isInternal = isInternal,
        label = label,
        creationDate = creationDate,
        lastTimeUsed = null
    )
}

