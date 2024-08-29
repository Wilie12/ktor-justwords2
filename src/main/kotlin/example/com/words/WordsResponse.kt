package example.com.words

import example.com.words.entity.WordSerializable
import kotlinx.serialization.Serializable

@Serializable
data class WordsResponse(
    val words: List<WordSerializable>
)
