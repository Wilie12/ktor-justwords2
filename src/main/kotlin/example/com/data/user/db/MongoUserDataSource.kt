package example.com.data.user.db

import example.com.data.user.db.entity.User
import example.com.data.user.db.entity.UserInfo
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class MongoUserDataSource(
    db: CoroutineDatabase
): UserDataSource {

    private val users = db.getCollection<User>()
    private val usersInfo = db.getCollection<UserInfo>()

    override suspend fun getUserByEmail(email: String): User? {
        return users.findOne(User::email eq email)
    }

    override suspend fun getUserById(userId: String): User? {
        return users.findOne(User::id eq ObjectId(userId))
    }

    override suspend fun insertUser(user: User): Boolean {
        val wasAcknowledged = users.insertOne(user).wasAcknowledged()
        val wasInfoAcknowledged = usersInfo.insertOne(
            UserInfo(
                dailyStreak = 0,
                bestStreak = 0,
                currentGoal = 0,
                dailyGoal = 4,
                lastPlayedTimestamp = "",
                userName = user.username,
                userId = user.id
            )
        ).wasAcknowledged()

        return wasAcknowledged && wasInfoAcknowledged
    }

    override suspend fun getUserInfo(userId: String): UserInfo? {
        return usersInfo.findOne(UserInfo::userId eq ObjectId(userId))
    }

    override suspend fun updateUserInfo(userInfo: UserInfo): Boolean {
        return usersInfo.updateOne(UserInfo::userId eq userInfo.userId, userInfo).wasAcknowledged()
    }
}