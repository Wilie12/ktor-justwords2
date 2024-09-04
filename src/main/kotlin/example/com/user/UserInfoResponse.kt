package example.com.user

import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponse(
    val userInfo: UserInfoSerializable
)
