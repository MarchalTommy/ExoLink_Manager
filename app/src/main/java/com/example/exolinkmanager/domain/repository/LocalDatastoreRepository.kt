package com.example.exolinkmanager.domain.repository

import com.example.exolinkmanager.ui.models.Deeplink
import com.google.common.collect.ImmutableMap
import kotlinx.coroutines.flow.Flow

interface LocalDatastoreRepository {

    suspend fun updateDeeplinkFavorite(deeplinkId: String)

    fun getFavoritesDeeplink(): Flow<List<String>>

    suspend fun setLastUsedDeeplink(
        deeplinkList: List<Deeplink>
    )

    fun getLastUsedDeeplinksIds(): Flow<Map<String, Int>>

    suspend fun incrementDeeplinkNumberOfUse(deeplink: Deeplink)

    fun getDeeplinkByNumberOfUse(): Flow<Map<String, Int>>
}