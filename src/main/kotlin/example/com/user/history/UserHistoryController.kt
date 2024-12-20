package example.com.user.history

import example.com.user.data.db.UserDataSource
import example.com.user.data.db.UserHistoryDataSource
import example.com.user.data.db.entity.UserWordHistory
import example.com.util.DataError
import example.com.util.EmptyResult
import example.com.util.Result

class UserHistoryController(
    private val userHistoryDataSource: UserHistoryDataSource,
    private val userDataSource: UserDataSource
) {

    suspend fun getAllUserHistoryById(userId: String): Result<List<UserWordHistorySerializable>, DataError.Insert> {
        if (userDataSource.getUserById(userId) == null) {
            return Result.Error(DataError.Insert.USER_DOES_NOT_EXISTS)
        }

        val userHistoryItems = userHistoryDataSource.getAllUserHistoryById(userId)

        return Result.Success(
            userHistoryItems.map { it.toUserWordHistorySerializable() }
        )
    }

    suspend fun insertUserHistory(wordHistory: UserWordHistory): EmptyResult<DataError.Insert> {
        if (userDataSource.getUserById(wordHistory.userId) == null) {
            return Result.Error(DataError.Insert.USER_DOES_NOT_EXISTS)
        }

        if (!userHistoryDataSource.insertUserHistory(wordHistory)) {
            return Result.Error(DataError.Insert.COULD_NOT_INSERT)
        }

        return Result.Success(Unit)
    }
}