package example.com.user.history

import kotlinx.serialization.Serializable

@Serializable
data class UserWordHistorySerializable(
    val bookName: String,
    val bookColor: Int,
    val setName: String,
    val groupNumber: Int,
    val dateTimeUtc: String,
    val perfectGuessed: Int,
    val wordListSize: Int,
    val id: String
)