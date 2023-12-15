package com.example.exolinkmanager.domain.usecase

import com.example.exolinkmanager.domain.repository.LocalDatastoreRepository
import javax.inject.Inject

class IncrementDeeplinkUseUseCase @Inject constructor(
    private val localDatastoreRepository: LocalDatastoreRepository
){
    suspend operator fun invoke(deeplinkId: String) {
        localDatastoreRepository.incrementDeeplinkNumberOfUse(deeplinkId)
    }
}