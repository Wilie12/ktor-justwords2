package example.com.data.user.hashing

data class SaltedHash(
    val hash: String,
    val salt: String
)
