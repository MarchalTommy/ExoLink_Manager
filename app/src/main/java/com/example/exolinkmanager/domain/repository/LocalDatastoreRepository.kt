package com.example.exolinkmanager.domain.repository

import com.example.exolinkmanager.ui.models.Deeplink
import kotlinx.coroutines.flow.Flow

interface LocalDatastoreRepository {

    suspend fun updateDeeplinkFavorite(deeplinkId: String)

    suspend fun getFavoritesDeeplink(): Flow<List<String>>

    suspend fun setLastUsedDeeplink(
        deeplinkList: List<Deeplink>
    )

    suspend fun getLastUsedDeeplinksIds(): Flow<List<String>>
}