package example.com.user.data.token.refresh

data class TokenUser(
    val userId: String,
    val token: String,
    val timeStamp: Long
)
