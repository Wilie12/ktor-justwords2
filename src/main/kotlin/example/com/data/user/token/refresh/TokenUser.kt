package example.com.data.user.token.refresh

data class TokenUser(
    val userId: String,
    val token: String,
    val timeStamp: Long
)
