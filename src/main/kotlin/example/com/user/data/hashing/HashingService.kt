package example.com.user.data.hashing

interface HashingService {

    fun generateSaltedHash(value: String, saltLength: Int = 32): SaltedHash
    fun verifyHash(value: String, saltedHash: SaltedHash): Boolean
}