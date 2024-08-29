package example.com.words

import example.com.words.entity.WordSetSerializable
import kotlinx.serialization.Serializable

@Serializable
data class WordSetResponse(
    val sets: List<WordSetSerializable>
)
