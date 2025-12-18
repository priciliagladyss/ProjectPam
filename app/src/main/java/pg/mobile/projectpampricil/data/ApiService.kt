package pg.mobile.projectpampricil.data

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // Ambil semua place
    @GET("projects/my-pam-test/databases/(default)/documents/places")
    suspend fun getPlaces(): FirestoreResponse<PlaceDocument>

    // Ambil semua komentar 1 tempat
    @GET("projects/my-pam-test/databases/(default)/documents/places/{place}/comments")
    suspend fun getComments(
        @Path("place") placeName: String
    ): FirestoreResponse<CommentDocument>

    // Tambah komentar baru
    @POST("projects/my-pam-test/databases/(default)/documents/places/{place}/comments")
    suspend fun addComment(
        @Path("place") placeName: String,
        @Body comment: CommentDocument
    ): CommentDocument

    // Update rating tempat
    @PATCH("projects/my-pam-test/databases/(default)/documents/places/{place}")
    suspend fun updateRating(
        @Path("place") placeName: String,
        @Query("updateMask.fieldPaths") fieldPath: String = "rating",
        @Body data: PlaceDocument
    ): PlaceDocument

    // Ambil semua favorit user
    @GET("projects/my-pam-test/databases/(default)/documents/users/{uid}")
    suspend fun getUserFavorites(
        @Path("uid") uid: String
    ): UserFavoriteDocument

    // Set/Update favorit user
    @PATCH("projects/my-pam-test/databases/(default)/documents/users/{uid}")
    suspend fun updateUserFavorites(
        @Path("uid") uid: String,
        @Query("updateMask.fieldPaths") fieldPath: String = "favorites",
        @Body data: UserFavoriteDocument
    ): UserFavoriteDocument

    // Get user profile
    @GET("projects/my-pam-test/databases/(default)/documents/users/{uid}")
    suspend fun getUser(
        @Path("uid") uid: String
    ): UserDocuments

    // Create/Update user profile
    @PATCH("projects/my-pam-test/databases/(default)/documents/users/{uid}")
    suspend fun createUserProfile(
        @Path("uid") uid: String,
        @Body userData: UserDocuments
    ): UserDocuments
}

// Response wrapper untuk list documents
data class FirestoreResponse<T>(
    val documents: List<T>? = null
)

// Document struktur untuk Place
data class PlaceDocument(
    val name: String? = null,
    val fields: PlaceFields? = null
)

data class PlaceFields(
    val name: StringValue? = null,
    val description: StringValue? = null,
    val rating: DoubleValue? = null,
    val theme: StringValue? = null
)

// Document struktur untuk Comment
data class CommentDocument(
    val name: String? = null,
    val fields: CommentFields? = null
)

data class CommentFields(
    val text: StringValue? = null,
    val rating: IntegerValue? = null,
    val timestamp: TimestampValue? = null
)

// Document struktur untuk User Favorites
data class UserFavoriteDocument(
    val name: String? = null,
    val fields: UserFavoriteFields? = null
)

data class UserFavoriteFields(
    val favorites: FirestoreArray? = null
)

// Firestore value types
data class StringValue(val stringValue: String? = null)
data class DoubleValue(val doubleValue: Double? = null)
data class IntegerValue(val integerValue: String? = null)
data class TimestampValue(val timestampValue: String? = null)
data class FirestoreArray(val arrayValue: FirestoreArrayContent? = null)
data class FirestoreArrayContent(val values: List<StringValue>? = null)

// User Documents (sudah ada di UserViewModel.kt)
data class UserDocuments(
    val name: String? = null,
    val fields: UserFields? = null
)

data class UserFields(
    val userName: StringValue? = null,
    val email: StringValue? = null,
    val totalPoints: IntegerValue? = null,
    val completedMissions: FirestoreArray? = null
)