package com.example.exolinkmanager.data.repository

import com.example.exolinkmanager.ui.models.Deeplink
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.coroutineScope

class FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun fetchDeeplinks(onCompletion: (List<Deeplink>?) -> Unit) {
        coroutineScope {
            db.collection("deeplinks").get().addOnCompleteListener { task ->
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

    // TODO: Finish this method
    suspend fun addDeeplink(deeplink: Deeplink, onCompletion: (Boolean) -> Unit) {
        coroutineScope {
            db.collection("deeplinks").add(deeplink).addOnCompleteListener { task ->
                onCompletion.invoke(task.isSuccessful)
            }
        }
    }
}