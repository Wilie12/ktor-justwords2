package example.com.words.entity

import kotlinx.serialization.Serializable

@Serializable
data class WordSetSerializable(
    val name: String,
    val bookId: String,
    val numberOfGroups: Int,
    val id: String
)
