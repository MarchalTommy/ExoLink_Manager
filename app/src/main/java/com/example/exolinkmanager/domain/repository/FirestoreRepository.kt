package com.example.exolinkmanager.domain.repository

import com.example.exolinkmanager.data.remote.repository.model.RemoteDeeplink
import com.example.exolinkmanager.domain.model.BusinessDeeplink

interface FirestoreRepository {

    suspend fun fetchDeeplinks(onCompletion: (List<BusinessDeeplink>?) -> Unit)

    suspend fun addDeeplink(deeplink: RemoteDeeplink, onCompletion: (Boolean) -> Unit)

    suspend fun removeDeeplink(deeplink: RemoteDeeplink, onCompletion: (Boolean) -> Unit)

    suspend fun editDeeplink(deeplink: RemoteDeeplink, onCompletion: (Boolean) -> Unit)
}