package example.com.user.data.hashing

data class SaltedHash(
    val hash: String,
    val salt: String
)
