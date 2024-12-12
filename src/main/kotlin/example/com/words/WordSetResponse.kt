package example.com.words

import example.com.words.model.WordSetSerializable
import kotlinx.serialization.Serializable

@Serializable
data class WordSetResponse(
    val sets: List<WordSetSerializable>
)
