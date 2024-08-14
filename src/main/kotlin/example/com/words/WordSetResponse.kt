package example.com.words

import kotlinx.serialization.Serializable

@Serializable
data class WordSetResponse(
    val sets: List<WordSetSerializable>
)
