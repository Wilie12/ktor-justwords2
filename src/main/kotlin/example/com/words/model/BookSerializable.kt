package example.com.words.model

import kotlinx.serialization.Serializable

@Serializable
data class BookSerializable(
    val name: String,
    val color: Int,
    val id: String
)
