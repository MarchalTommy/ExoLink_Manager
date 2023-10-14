package com.example.exolinkmanager.domain.model

import com.example.exolinkmanager.data.local.model.LocalDeeplink
import com.example.exolinkmanager.data.remote.repository.model.RemoteDeeplink
import com.example.exolinkmanager.ui.models.Deeplink
import com.google.firebase.Timestamp

data class BusinessDeeplink(

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
     * Number of times the deeplink was used. Used only for sorting
     */
    var numberOfTimesUsed: Long = 0L,

    /**
     * Date of creation of the deeplink, used for sorting
     */
    var creationDate: Timestamp = Timestamp.now()
)

fun BusinessDeeplink.toLocalDeeplink(): LocalDeeplink {
    return LocalDeeplink(
        id = id,
        schema = schema,
        path = path,
        isInternal = isInternal,
        label = label,
        creationDate = creationDate,
        lastTimeUsed = null,
        numberOfTimesUsed = 0L
    )
}

fun BusinessDeeplink.toRemoteDeeplink(): RemoteDeeplink {
    return RemoteDeeplink(
        id = id,
        schema = schema,
        path = path,
        isInternal = isInternal,
        label = label,
        creationDate = creationDate
    )
}

fun BusinessDeeplink.toDeeplink(): Deeplink {
    return Deeplink(
        id = id,
        schema = schema,
        path = path,
        isInternal = isInternal,
        label = label,
        creationDate = creationDate,
        lastTimeUsed = lastTimeUsed,
        numberOfTimesUsed = numberOfTimesUsed
    )
}