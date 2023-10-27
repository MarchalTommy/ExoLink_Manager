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

    /**
     * Sadly we can't store map in datastore, so I "create" my own map with a string and an int
     * concatenated with a slash. The string is the deeplink id and the int is its index in the
     * list of last used deeplink. This way I can store the last used deeplink in the datastore
     * and keep the order of the list when I get them out.
     *
     * Kind of nasty, but it works.
     *
     * @param deeplinkList
     */
    override suspend fun setLastUsedDeeplink(
        deeplinkList: List<Deeplink>
    ) {
        Result.runCatching {
            datastore.edit { preferences ->
                val sortedList = deeplinkList.sortedByDescending {
                    it.lastTimeUsed
                }
                preferences[LAST_USED_DEEPLINK_KEY] =
                    sortedList.map {
                        it.id + "/${
                            sortedList.indexOf(it)
                        }"
                    }.toSet()
            }
        }
    }

    override suspend fun getLastUsedDeeplinksIds(): Flow<Map<String, Int>> {
        return datastore.data.map { preferences ->
            val map = mutableMapOf<String, Int>()
            val orderedList = preferences[LAST_USED_DEEPLINK_KEY]?.sortedByDescending { it.last() }
            orderedList?.forEach { map[it.split('/')[0]] = it.split('/')[1].toInt() }
            map
        }
    }

}