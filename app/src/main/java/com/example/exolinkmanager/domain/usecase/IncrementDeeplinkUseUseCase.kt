package com.example.exolinkmanager.domain.usecase

import com.example.exolinkmanager.domain.repository.LocalDatastoreRepository
import com.example.exolinkmanager.ui.models.Deeplink
import javax.inject.Inject

class IncrementDeeplinkUseUseCase @Inject constructor(
    private val localDatastoreRepository: LocalDatastoreRepository
) {
    suspend operator fun invoke(deeplink: Deeplink) {
        localDatastoreRepository.incrementDeeplinkNumberOfUse(deeplink)
    }
}