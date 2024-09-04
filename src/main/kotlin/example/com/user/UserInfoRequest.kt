package example.com.user

import kotlinx.serialization.Serializable

@Serializable
data class UserInfoRequest(
    val userInfo: UserInfoSerializable
)
