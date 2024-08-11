package example.com.data.user.db

import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class MongoDataSource(
    db: CoroutineDatabase
): UserDataSource {

    private val users = db.getCollection<User>()

    override suspend fun getUserByEmail(email: String): User? {
        return users.findOne(User::email eq email)
    }

    override suspend fun getUserById(userId: String): User? {
        return users.findOne(User::id eq ObjectId(userId))
    }

    override suspend fun insertUser(user: User): Boolean {
        return users.insertOne(user).wasAcknowledged()
    }
}