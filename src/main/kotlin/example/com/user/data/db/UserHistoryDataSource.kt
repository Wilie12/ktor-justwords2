package example.com.user.data.db

import example.com.user.data.db.entity.UserWordHistory

interface UserHistoryDataSource {

    suspend fun getAllUserHistoryById(userId: String): List<UserWordHistory>
    suspend fun insertUserHistory(userWordHistory: UserWordHistory): Boolean
}