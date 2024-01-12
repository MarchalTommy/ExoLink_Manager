package com.example.exolinkmanager.domain.usecase

import com.example.exolinkmanager.domain.repository.LocalDatastoreRepository
import javax.inject.Inject

class GetFavoritesDeeplinkUseCase @Inject constructor(
    private val datastoreRepository: LocalDatastoreRepository
) {
    operator fun invoke() = datastoreRepository.getFavoritesDeeplink()
}