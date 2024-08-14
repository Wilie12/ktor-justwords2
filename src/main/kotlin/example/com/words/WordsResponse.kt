package example.com.words

import kotlinx.serialization.Serializable

@Serializable
data class WordsResponse(
    val words: List<WordSerializable>
)
