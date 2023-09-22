package com.example.exolinkmanager.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.exolinkmanager.data.local.repository.LocalDatastoreRepositoryImpl
import com.example.exolinkmanager.data.remote.repository.FirestoreRepositoryImpl
import com.example.exolinkmanager.domain.repository.FirestoreRepository
import com.example.exolinkmanager.domain.repository.LocalDatastoreRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

val Context.myDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "myDatastore"
)

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    abstract fun bindFirestoreRepository(firestoreRepositoryImpl: FirestoreRepositoryImpl): FirestoreRepository

    @Binds
    abstract fun bindDatastoreRepository(localDatastoreRepositoryImpl: LocalDatastoreRepositoryImpl): LocalDatastoreRepository

    companion object {
        // provides instance of DataStore
        @Provides
        @Singleton
        fun provideUserDataStorePreferences(
            @ApplicationContext applicationContext: Context
        ): DataStore<Preferences> {
            return applicationContext.myDataStore
        }
    }
}
