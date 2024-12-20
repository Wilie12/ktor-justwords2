package example.com.user.data.db

import example.com.user.data.db.entity.User
import example.com.user.data.db.entity.UserInfo
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import java.time.ZoneId
import java.time.ZonedDateTime

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

    override suspend fun insertUser(user: User, username: String): Boolean {
        val wasAcknowledged = users.insertOne(user).wasAcknowledged()
        val wasInfoAcknowledged = usersInfo.insertOne(
            UserInfo(
                dailyStreak = 0,
                bestStreak = 0,
                currentGoal = 0,
                dailyGoal = 4,
                lastPlayedTimestamp = ZonedDateTime.now()
                    .withZoneSameInstant(ZoneId.of("UTC")).toInstant().toString(),
                lastEditedTimestamp = ZonedDateTime.now()
                    .withZoneSameInstant(ZoneId.of("UTC")).toInstant().toString(),
                username = username,
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