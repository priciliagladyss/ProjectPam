package pg.mobile.projectpampricil.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.tasks.await

class PlaceRepositoryFirestore(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    suspend fun fetchPlaces(): List<PlaceModel> {
        val snap = db.collection("places").get().await()
        return snap.documents.mapNotNull { it.toObject(PlaceModel::class.java) }
    }

    suspend fun fetchComments(placeId: String): List<Comment> {
        val snap = db.collection("places")
            .document(placeId)
            .collection("comments")
            .get()
            .await()
        return snap.documents.mapNotNull { it.toObject(Comment::class.java) }
    }

    suspend fun addComment(placeId: String, text: String, rating: Int) {
        db.collection("places")
            .document(placeId)
            .collection("comments")
            .add(
                Comment(
                    text = text,
                    rating = rating
                )
            )
            .await()
    }

    suspend fun updateRating(placeId: String, rating: Double) {
        db.collection("places")
            .document(placeId)
            .update("rating", rating)
            .await()
    }

    suspend fun fetchFavorites(): Set<String> {
        val uid = auth.currentUser?.uid ?: return emptySet()
        val snap = db.collection("users").document(uid)
            .collection("favorites")
            .get()
            .await()
        return snap.documents.map { it.id }.toSet()
    }

    suspend fun setFavorite(placeId: String, isFavorite: Boolean) {
        val uid = auth.currentUser?.uid ?: return
        val ref = db.collection("users").document(uid)
            .collection("favorites")
            .document(placeId)

        if (isFavorite) {
            ref.set(mapOf("value" to true)).await()
        } else {
            ref.delete().await()
        }
    }
}
