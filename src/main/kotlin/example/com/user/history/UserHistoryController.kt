package example.com.user.history

import example.com.data.user.db.UserDataSource
import example.com.data.user.db.UserHistoryDataSource
import example.com.data.user.db.entity.UserWordHistory
import example.com.util.DataError
import example.com.util.EmptyResult
import example.com.util.Result

class UserHistoryController(
    private val userHistoryDataSource: UserHistoryDataSource,
    private val userDataSource: UserDataSource
) {

    suspend fun getAllUserHistoryById(userId: String): Result<UserHistoryResponse, DataError.Auth> {
        if (userDataSource.getUserById(userId) == null) {
            return Result.Error(DataError.Auth.USER_DOES_NOT_EXISTS)
        }

        val userHistoryItems = userHistoryDataSource.getAllUserHistoryById(userId)

        return Result.Success(
            UserHistoryResponse(
                userHistoryItems = userHistoryItems.map { it.toUserWordHistorySerializable() }
            )
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