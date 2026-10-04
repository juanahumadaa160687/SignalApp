package com.jpa.signal.data

import com.google.firebase.database.*
import kotlinx.coroutines.tasks.await

data class Phrase(
    val userUid: String = "",
    val phrase: List<String> = emptyList()
)

class FrasesRepo {

    companion object{
        private var database: DatabaseReference = FirebaseDatabase.getInstance().getReference("frases")

        suspend fun getFrasesByUid(uid: String): Phrase? {
            val snapshot = database.child(uid).get().await()
            return snapshot.getValue(Phrase::class.java)
        }

        suspend fun deleteFrase(uid: String) {
            database.child(uid).removeValue().await()
        }

        suspend fun updateFrase(phrase: Phrase) {
            database.child(phrase.userUid).setValue(phrase).await()
        }

        suspend fun addFrase(phrase: Phrase) {
            database.child(phrase.userUid).setValue(phrase).await()

        }
    }
}