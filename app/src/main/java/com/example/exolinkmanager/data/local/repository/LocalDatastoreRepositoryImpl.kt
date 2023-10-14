package com.example.exolinkmanager.data.local.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.example.exolinkmanager.domain.repository.LocalDatastoreRepository
import com.example.exolinkmanager.ui.models.Deeplink
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDatastoreRepositoryImpl @Inject constructor(
    private val datastore: DataStore<Preferences>
) : LocalDatastoreRepository {

    private val FAVORITE_DEEPLINK_LIST_KEY = stringSetPreferencesKey("favorite_list")
    private val LAST_USED_DEEPLINK_KEY = stringSetPreferencesKey("last_used_deeplink")

    override suspend fun updateDeeplinkFavorite(
        deeplinkId: String
    ) {
        Result.runCatching {
            datastore.edit { preferences ->
                preferences[FAVORITE_DEEPLINK_LIST_KEY]?.find {
                    it.equals(deeplinkId, true)
                }?.let {
                    preferences[FAVORITE_DEEPLINK_LIST_KEY] =
                        preferences[FAVORITE_DEEPLINK_LIST_KEY]?.toMutableSet()?.apply {
                            remove(it)
                        } ?: run {
                            mutableSetOf()
                        }
                } ?: run {
                    preferences[FAVORITE_DEEPLINK_LIST_KEY] =
                        preferences[FAVORITE_DEEPLINK_LIST_KEY]?.toMutableSet()?.apply {
                            add(deeplinkId)
                        } ?: run {
                            mutableSetOf(deeplinkId)
                        }
                }
            }
        }
    }

    override suspend fun getFavoritesDeeplink(): Flow<List<String>> {
        return datastore.data.map { preferences ->
            preferences[FAVORITE_DEEPLINK_LIST_KEY]?.toList() ?: listOf()
        }
    }

    override suspend fun setLastUsedDeeplink(
        deeplinkList: List<Deeplink>
    ) {
        Result.runCatching {
            datastore.edit { preferences ->
                preferences[LAST_USED_DEEPLINK_KEY] = deeplinkList.sortedByDescending {
                    it.lastTimeUsed
                }.mapNotNull { it.id }.toSet()
            }
        }
    }

    override suspend fun getLastUsedDeeplinksIds(): Flow<List<String>> {
        return datastore.data.map { preferences ->
            preferences[LAST_USED_DEEPLINK_KEY]?.toList() ?: listOf()
        }
    }

}