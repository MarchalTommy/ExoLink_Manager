package com.example.exolinkmanager.di

import com.example.exolinkmanager.data.repository.FirestoreRepositoryImpl
import com.example.exolinkmanager.domain.repository.FirestoreRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@InstallIn(ActivityRetainedComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    abstract fun bindFirestoreRepository(firestoreRepositoryImpl: FirestoreRepositoryImpl): FirestoreRepository
}