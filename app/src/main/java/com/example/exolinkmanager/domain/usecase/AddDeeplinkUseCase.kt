package com.example.exolinkmanager.domain.usecase

import com.example.exolinkmanager.domain.model.BusinessDeeplink
import com.example.exolinkmanager.domain.model.toRemoteDeeplink
import com.example.exolinkmanager.domain.repository.FirestoreRepository
import com.example.exolinkmanager.ui.models.Deeplink
import javax.inject.Inject

class AddDeeplinkUseCase @Inject constructor(
    private val repository: FirestoreRepository
) {

    suspend fun invoke(deeplink: BusinessDeeplink, onCompletion: (Boolean) -> Unit) {
        repository.addDeeplink(deeplink.toRemoteDeeplink(), onCompletion)
    }
}