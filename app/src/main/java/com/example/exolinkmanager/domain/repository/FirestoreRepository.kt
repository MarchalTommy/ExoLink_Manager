package com.example.exolinkmanager.domain.repository

import com.example.exolinkmanager.ui.models.Deeplink

interface FirestoreRepository {

    suspend fun fetchDeeplinks(onCompletion: (List<Deeplink>?) -> Unit)

    suspend fun addDeeplink(deeplink: Deeplink, onCompletion: (Boolean) -> Unit)

    suspend fun removeDeeplink(deeplink: Deeplink, onCompletion: (Boolean) -> Unit)

    suspend fun editDeeplink(deeplink: Deeplink, onCompletion: (Boolean) -> Unit)
}