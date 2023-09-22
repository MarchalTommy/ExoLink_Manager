package com.example.exolinkmanager.domain.usecase

import com.example.exolinkmanager.domain.repository.LocalDatastoreRepository
import com.example.exolinkmanager.ui.models.Deeplink
import javax.inject.Inject

class SetFavoriteStateUseCase @Inject constructor(
    private val datastoreRepository: LocalDatastoreRepository
) {

    suspend fun invoke(deeplink: Deeplink) {
        datastoreRepository.updateDeeplinkFavorite(deeplink.id ?: "")
    }
}