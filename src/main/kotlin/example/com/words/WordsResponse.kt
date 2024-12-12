package example.com.words

import example.com.words.model.WordSerializable
import kotlinx.serialization.Serializable

@Serializable
data class WordsResponse(
    val words: List<WordSerializable>
)
