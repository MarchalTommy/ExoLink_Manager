package com.example.exolinkmanager.domain.usecase

import com.example.exolinkmanager.domain.model.BusinessDeeplink
import com.example.exolinkmanager.domain.repository.FirestoreRepository
import javax.inject.Inject

class FetchDeeplinksUseCase @Inject constructor(
    private val repository: FirestoreRepository
) {

    suspend fun invoke(onCompletion: (List<BusinessDeeplink>?) -> Unit) {
        repository.fetchDeeplinks(onCompletion)
    }
}