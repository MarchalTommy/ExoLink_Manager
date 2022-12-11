package com.example.exolinkmanager.data.repository

import com.example.exolinkmanager.domain.repository.FirestoreRepository
import com.example.exolinkmanager.ui.models.Deeplink
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreRepositoryImpl @Inject constructor() : FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()

    override suspend fun fetchDeeplinks(onCompletion: (List<Deeplink>?) -> Unit) {
        coroutineScope {
            db.collection("deeplinks")
                .get()
                .addOnCompleteListener { task ->
                    onCompletion.invoke(
                        if (task.isSuccessful) {
                            val list = mutableListOf<Deeplink>()
                            for (document in task.result) {
                                list.add(
                                    Deeplink(
                                        id = document.id,
                                        schema = document.data["schema"] as String,
                                        isInternal = document.data["internal"] as Boolean,
                                        path = document.data["path"] as String,
                                        label = document.data["label"] as String
                                    )
                                )
                            }
                            list
                        } else {
                            null
                        }
                    )
                }
        }
    }

    override suspend fun addDeeplink(deeplink: Deeplink, onCompletion: (Boolean) -> Unit) {
        coroutineScope {
            db.collection("deeplinks")
                .add(deeplink)
                .addOnCompleteListener { task ->
                    onCompletion.invoke(task.isSuccessful)
                }
        }
    }

    override suspend fun removeDeeplink(deeplink: Deeplink, onCompletion: (Boolean) -> Unit) {
        coroutineScope {
            deeplink.id?.let {
                db.collection("deeplinks")
                    .document(it)
                    .delete()
                    .addOnCompleteListener { task ->
                        onCompletion.invoke(task.isSuccessful)
                    }
            }
        }
    }

    override suspend fun editDeeplink(deeplink: Deeplink, onCompletion: (Boolean) -> Unit) {
        coroutineScope {
            deeplink.id?.let {
                db.collection("deeplinks")
                    .document(it)
                    .set(deeplink)
                    .addOnCompleteListener { task ->
                        onCompletion.invoke(task.isSuccessful)
                    }
            }
        }
    }
}