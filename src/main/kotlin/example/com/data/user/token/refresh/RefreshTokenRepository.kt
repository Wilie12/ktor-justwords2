package example.com.data.user.token.refresh

interface RefreshTokenRepository {

    suspend fun upsert(tokenUser: TokenUser)

    suspend fun getUserById(userId: String): TokenUser?
}