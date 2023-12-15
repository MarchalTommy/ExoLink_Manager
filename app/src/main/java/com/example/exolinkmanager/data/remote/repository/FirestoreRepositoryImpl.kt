package com.example.exolinkmanager.data.remote.repository

import com.example.exolinkmanager.data.remote.repository.model.RemoteDeeplink
import com.example.exolinkmanager.data.remote.repository.model.toBusinessDeeplink
import com.example.exolinkmanager.domain.model.BusinessDeeplink
import com.example.exolinkmanager.domain.repository.FirestoreRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreRepositoryImpl @Inject constructor(

) : FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()

    override suspend fun fetchDeeplinks(onCompletion: (List<BusinessDeeplink>?) -> Unit) {
        coroutineScope {
            db.collection("deeplinks")
                .get()
                .addOnCompleteListener { task ->
                    onCompletion.invoke(
                        if (task.isSuccessful) {
                            val list = mutableListOf<RemoteDeeplink>()
                            for (document in task.result) {
                                list.add(
                                    RemoteDeeplink(
                                        id = document.id,
                                        schema = document.data["schema"] as String,
                                        isInternal = document.data["internal"] as Boolean,
                                        path = document.data["path"] as String,
                                        label = document.data["label"] as String,
                                        creationDate = document.data["creationDate"] as Timestamp?
                                    )
                                )
                            }
                            list.map { it.toBusinessDeeplink() }
                        } else {
                            null
                        }
                    )
                }
        }
    }

    override suspend fun addDeeplink(deeplink: RemoteDeeplink, onCompletion: (Boolean) -> Unit) {
        coroutineScope {
            deeplink.creationDate = Timestamp.now()
            db.collection("deeplinks")
                .add(deeplink)
                .addOnCompleteListener { task ->
                    onCompletion.invoke(task.isSuccessful)
                }
        }
    }

    override suspend fun removeDeeplink(deeplink: RemoteDeeplink, onCompletion: (Boolean) -> Unit) {
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

    override suspend fun editDeeplink(deeplink: RemoteDeeplink, onCompletion: (Boolean) -> Unit) {
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