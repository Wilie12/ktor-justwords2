package example.com.words.model

import kotlinx.serialization.Serializable

@Serializable
data class WordSerializable(
    val sentence: String,
    val wordPl: String,
    val wordEng: String,
    val setId: String,
    val groupNumber: Int,
    val id: String
)
