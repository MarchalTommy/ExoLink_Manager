package com.example.exolinkmanager.domain.usecase

import com.example.exolinkmanager.domain.repository.FirestoreRepository
import com.example.exolinkmanager.ui.models.Deeplink
import javax.inject.Inject

class RemoveDeeplinkUseCase @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) {
    suspend operator fun invoke(deeplink: Deeplink, onCompletion: (Boolean) -> Unit) {
        firestoreRepository.removeDeeplink(deeplink, onCompletion)
    }
}