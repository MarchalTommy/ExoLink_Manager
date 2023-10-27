package com.example.exolinkmanager.domain.usecase

import com.example.exolinkmanager.domain.repository.LocalDatastoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLastUsedDeeplinksIdsUseCase @Inject constructor(
    private val localDatastoreRepository: LocalDatastoreRepository
) {
    suspend operator fun invoke(): Flow<Map<String, Int>> {
        return localDatastoreRepository.getLastUsedDeeplinksIds()
    }
}