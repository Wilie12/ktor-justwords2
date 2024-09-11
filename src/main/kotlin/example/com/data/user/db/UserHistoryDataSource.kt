package example.com.data.user.db

import example.com.data.user.db.entity.UserWordHistory

interface UserHistoryDataSource {

    suspend fun getAllUserHistoryById(userId: String): List<UserWordHistory>
    suspend fun insertUserHistory(userWordHistory: UserWordHistory): Boolean
}