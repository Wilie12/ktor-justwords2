package example.com.words

import kotlinx.serialization.Serializable

@Serializable
data class WordRequest(
    val setId: String
)
