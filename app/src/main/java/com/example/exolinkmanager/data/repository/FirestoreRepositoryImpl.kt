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
                            task.result?.toObjects(Deeplink::class.java) ?: emptyList()
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
            db.collection("deeplinks")
                .whereEqualTo("label", deeplink.label)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.result?.documents?.forEach { document ->
                            db.collection("deeplinks")
                                .document(document.id)
                                .delete()
                                .addOnCompleteListener { task ->
                                    onCompletion.invoke(task.isSuccessful)
                                }
                        }
                    } else {
                        onCompletion.invoke(false)
                    }
                }
        }
    }
}