package example.com.user.data.token.refresh

interface RefreshTokenRepository {

    suspend fun upsert(tokenUser: TokenUser)

    suspend fun getUserById(userId: String): TokenUser?
}