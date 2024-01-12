package com.example.exolinkmanager.domain.usecase

import com.example.exolinkmanager.domain.model.BusinessDeeplink
import com.example.exolinkmanager.domain.repository.LocalDatastoreRepository
import javax.inject.Inject

class SetFavoriteStateUseCase @Inject constructor(
    private val datastoreRepository: LocalDatastoreRepository
) {

    suspend fun invoke(deeplink: BusinessDeeplink) {
        datastoreRepository.updateDeeplinkFavorite(deeplink.id ?: "")
    }
}