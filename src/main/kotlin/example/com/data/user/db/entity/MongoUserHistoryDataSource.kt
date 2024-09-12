package example.com.data.user.db.entity

import example.com.data.user.db.UserHistoryDataSource
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class MongoUserHistoryDataSource(
    db: CoroutineDatabase
): UserHistoryDataSource {
    private val usersHistory = db.getCollection<UserWordHistory>()

    override suspend fun getAllUserHistoryById(userId: String): List<UserWordHistory> {
        return usersHistory.find(UserWordHistory::userId eq userId).toList()
    }

    override suspend fun insertUserHistory(userWordHistory: UserWordHistory): Boolean {
        val wasAcknowledged = usersHistory.insertOne(userWordHistory).wasAcknowledged()
        return wasAcknowledged
    }
}