package com.example.exolinkmanager.domain.usecase

import com.example.exolinkmanager.domain.model.BusinessDeeplink
import com.example.exolinkmanager.domain.model.toRemoteDeeplink
import com.example.exolinkmanager.domain.repository.FirestoreRepository
import com.example.exolinkmanager.ui.models.Deeplink
import javax.inject.Inject

class RemoveDeeplinkUseCase @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) {
    suspend operator fun invoke(deeplink: BusinessDeeplink, onCompletion: (Boolean) -> Unit) {
        firestoreRepository.removeDeeplink(deeplink.toRemoteDeeplink(), onCompletion)
    }
}