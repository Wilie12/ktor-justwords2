package example.com.user.history

import kotlinx.serialization.Serializable

@Serializable
data class UserHistoryRequest(
    val userWordHistorySerializable: UserWordHistorySerializable
)
