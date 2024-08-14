package example.com.words

import kotlinx.serialization.Serializable

@Serializable
data class WordSetRequest(
    val bookId: String
)
