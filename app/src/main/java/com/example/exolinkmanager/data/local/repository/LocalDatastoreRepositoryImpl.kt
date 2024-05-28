package com.example.exolinkmanager.data.local.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.example.exolinkmanager.domain.repository.LocalDatastoreRepository
import com.example.exolinkmanager.ui.models.Deeplink
import com.google.common.collect.ImmutableMap
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDatastoreRepositoryImpl @Inject constructor(
    private val datastore: DataStore<Preferences>
) : LocalDatastoreRepository {

    private val gson = Gson()

    private val FAVORITE_DEEPLINK_LIST_KEY = stringSetPreferencesKey("favorite_list")
    private val LAST_USED_DEEPLINK_KEY = stringPreferencesKey("last_used_deeplink")
    private val NUMBER_OF_USE_DEEPLINK_KEY = stringPreferencesKey("number_use_deeplink")

    override suspend fun updateDeeplinkFavorite(
        deeplinkId: String
    ) {
        Result.runCatching {
            datastore.edit { preferences ->
                preferences[FAVORITE_DEEPLINK_LIST_KEY]?.find {
                    it.equals(
                        deeplinkId,
                        true
                    )
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

    override fun getFavoritesDeeplink(): Flow<List<String>> {
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
                preferences[LAST_USED_DEEPLINK_KEY] = gson.toJson(sortedList.toSet())
            }
        }
    }

    override fun getLastUsedDeeplinksIds(): Flow<Map<String, Int>> {
        return datastore.data.mapNotNull { preferences ->
            val map = mutableMapOf<String, Int>()

            val listOfDeeplinkObject: Type = object : TypeToken<ArrayList<Deeplink?>?>() {}.type

            val list: List<Deeplink?>? = try {
                gson.fromJson<List<Deeplink?>?>(
                    preferences[LAST_USED_DEEPLINK_KEY],
                    listOfDeeplinkObject
                )
            } catch (e: NullPointerException) {
                emptyList()
            }

            val orderedList = list?.sortedByDescending { it?.lastTimeUsed }
            orderedList?.forEach { map[it?.id ?: ""] = orderedList.indexOf(it) }

            map
        }
    }

    override suspend fun incrementDeeplinkNumberOfUse(deeplink: Deeplink) {
        Result.runCatching {
            datastore.edit { preferences ->
                val listOfDeeplinkObject: Type = object : TypeToken<ArrayList<Deeplink?>?>() {}.type
                val actualList = gson.fromJson<MutableList<Deeplink>>(
                    preferences[NUMBER_OF_USE_DEEPLINK_KEY],
                    listOfDeeplinkObject
                )

                actualList.find {
                    it.id.equals(
                        deeplink.id,
                        true
                    )
                }?.let {
                    actualList.remove(it)
                    actualList.add(it.apply {
                        this.numberOfTimeUsed = this.numberOfTimeUsed?.plus(1)
                    })
                } ?: run {
                    actualList.add(deeplink)
                }

                preferences[NUMBER_OF_USE_DEEPLINK_KEY] = gson.toJson(actualList.toSet())
            }
        }
    }

    override fun getDeeplinkByNumberOfUse(): Flow<Map<String, Int>> {
        return datastore.data.map { preferences ->
            val map = mutableMapOf<String, Int>()

            val listOfDeeplinkObject: Type = object : TypeToken<ArrayList<Deeplink?>?>() {}.type

            val list: List<Deeplink?>? = try {
                gson.fromJson<MutableList<Deeplink>>(
                    preferences[NUMBER_OF_USE_DEEPLINK_KEY],
                    listOfDeeplinkObject
                )
            } catch (e: NullPointerException) {
                emptyList()
            }

            val orderedList = list?.sortedByDescending { it?.numberOfTimeUsed }
            orderedList?.forEach { map[it?.id ?: ""] = orderedList.indexOf(it) }
            map
        }
    }

}