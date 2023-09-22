package com.example.exolinkmanager.domain.repository

import kotlinx.coroutines.flow.Flow

interface LocalDatastoreRepository {

    suspend fun updateDeeplinkFavorite(deeplinkId: String)

    suspend fun getFavoritesDeeplink(): Flow<List<String>>
}