package example.com.auth

import example.com.data.user.db.User
import example.com.data.user.db.UserDataSource
import example.com.data.user.hashing.HashingService
import example.com.util.DataError
import example.com.util.EmptyResult
import example.com.util.Result

class AuthController(
    private val userDataSource: UserDataSource,
    private val hashingService: HashingService
) {

    suspend fun register(
        username: String,
        email: String,
        password: String
    ): EmptyResult<DataError.Auth> {

        if (!validateCredentials(username, email, password)) {
            return Result.Error(DataError.Auth.INVALID_CREDENTIALS)
        }

        if (userDataSource.getUserByEmail(email) != null) {
            return Result.Error(DataError.Auth.USER_EXISTS)
        }

        val saltedHash = hashingService.generateSaltedHash(password)

        val user = User(
            username = username,
            email = email,
            password = password,
            salt = saltedHash.salt
        )

        if (!userDataSource.insertUser(user)) {
            return Result.Error(DataError.Auth.USER_EXISTS)
        }

        return Result.Success(Unit)
    }

    private fun validateCredentials(
        username: String,
        email: String,
        password: String
    ): Boolean {
        val isUsernameNotBlank = username.isNotBlank()
        val isEmailNotBlank = email.isNotBlank()
        val isPasswordNotBlank = password.isNotBlank()

        return isUsernameNotBlank && isEmailNotBlank && isPasswordNotBlank
    }
}