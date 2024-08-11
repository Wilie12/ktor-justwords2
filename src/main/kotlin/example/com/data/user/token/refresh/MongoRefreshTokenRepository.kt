package example.com.data.user.token.refresh

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class MongoRefreshTokenRepository(
    private val db: CoroutineDatabase
): RefreshTokenRepository {

    private val tokenUsers = db.getCollection<TokenUser>()

    override suspend fun upsert(tokenUser: TokenUser) {
        val tokenUserExists = getUserById(tokenUser.userId)
        if (tokenUserExists != null) {
            tokenUsers.updateOne(TokenUser::userId eq tokenUser.userId, tokenUser)
        } else {
            tokenUsers.insertOne(tokenUser)
        }
    }

    override suspend fun getUserById(userId: String): TokenUser? {
        return tokenUsers.findOne(TokenUser::userId eq userId)
    }
}