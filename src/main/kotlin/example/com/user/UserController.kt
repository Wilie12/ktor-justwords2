package example.com.user

import example.com.user.data.db.UserDataSource
import example.com.util.DataError
import example.com.util.EmptyResult
import example.com.util.Result

class UserController(
    private val userDataSource: UserDataSource
) {

    suspend fun getUserInfo(userId: String): Result<UserInfoResponse, DataError.Auth> {
        if (userDataSource.getUserById(userId) == null) {
            return Result.Error(DataError.Auth.USER_DOES_NOT_EXISTS)
        }

        val userInfo = userDataSource.getUserInfo(userId)
            ?: return Result.Error(DataError.Auth.USER_DOES_NOT_EXISTS)

        return Result.Success(
            UserInfoResponse(
                userInfo = userInfo.toUserInfoSerializable()
            )
        )
    }

    suspend fun updateUserInfo(userInfo: UserInfoSerializable): EmptyResult<DataError.Auth> {
        if (userDataSource.getUserById(userInfo.userId) == null) {
            return Result.Error(DataError.Auth.USER_DOES_NOT_EXISTS)
        }

        val oldUserInfo = userDataSource.getUserInfo(userInfo.userId)
            ?: return Result.Error(DataError.Auth.USER_DOES_NOT_EXISTS)

        if (oldUserInfo.toUserInfoSerializable() == userInfo) {
            return Result.Success(Unit)
        }

        val wasAcknowledged = userDataSource.updateUserInfo(userInfo.toUserInfo())

        return if (!wasAcknowledged) {
            Result.Error(DataError.Auth.USER_DOES_NOT_EXISTS)
        } else {
            Result.Success(Unit)
        }
    }
}