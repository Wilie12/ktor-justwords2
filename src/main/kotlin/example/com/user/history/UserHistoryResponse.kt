package example.com.user.history

import kotlinx.serialization.Serializable

@Serializable
data class UserHistoryResponse(
    val userHistoryItems: List<UserWordHistorySerializable>
)
