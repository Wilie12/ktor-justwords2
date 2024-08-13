package example.com.auth

import example.com.data.user.db.entity.User
import example.com.data.user.db.UserDataSource
import example.com.data.user.hashing.HashingService
import example.com.data.user.hashing.SaltedHash
import example.com.data.user.token.TokenClaim
import example.com.data.user.token.TokenConfig
import example.com.data.user.token.TokenService
import example.com.data.user.token.refresh.RefreshTokenRepository
import example.com.data.user.token.refresh.TokenUser
import example.com.util.DataError
import example.com.util.EmptyResult
import example.com.util.Result

class AuthController(
    private val userDataSource: UserDataSource,
    private val hashingService: HashingService,
    private val tokenService: TokenService,
    private val accessTokenConfig: TokenConfig,
    private val refreshTokenConfig: TokenConfig,
    private val refreshTokenRepository: RefreshTokenRepository
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
            password = saltedHash.hash,
            salt = saltedHash.salt
        )

        if (!userDataSource.insertUser(user)) {
            return Result.Error(DataError.Auth.USER_EXISTS)
        }

        return Result.Success(Unit)
    }

    suspend fun login(
        email: String,
        password: String
    ): Result<LoginResponse, DataError.Auth> {

        val user = userDataSource.getUserByEmail(email)
            ?: return Result.Error(DataError.Auth.USER_DOES_NOT_EXISTS)

        val isValidPassword = hashingService.verifyHash(
            value = password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )
        if (!isValidPassword) {
            return Result.Error(DataError.Auth.INVALID_CREDENTIALS)
        }

        val loginResponse = generateTokens(user)

        refreshTokenRepository.upsert(
            TokenUser(
                userId = user.id.toHexString(),
                token = loginResponse.refreshToken,
                timeStamp = System.currentTimeMillis()
            )
        )

        return Result.Success(loginResponse)
    }

    suspend fun accessToken(
        refreshToken: String,
        userId: String
    ): Result<AccessTokenResponse, DataError.Auth> {
        val user = userDataSource.getUserById(userId)
            ?: return Result.Error(DataError.Auth.USER_DOES_NOT_EXISTS)

        val tokenUser = refreshTokenRepository.getUserById(userId)
            ?: return Result.Error(DataError.Auth.USER_DOES_NOT_EXISTS)

        if (refreshToken != tokenUser.token) {
            return Result.Error(DataError.Auth.INVALID_CREDENTIALS)
        }

        val accessToken = refreshAccessToken(userId)

        return Result.Success(AccessTokenResponse(accessToken))
    }

    private fun refreshAccessToken(
        userId: String
    ): String {
        return tokenService.generate(
            config = accessTokenConfig,
            TokenClaim(
                name = "userId",
                value = userId
            ),
            TokenClaim(
                name = "tokenType",
                value = "accessToken"
            )
        )
    }

    private  fun generateTokens(user: User): LoginResponse {
        val accessToken = tokenService.generate(
            config = accessTokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id.toHexString()
            ),
            TokenClaim(
                name = "tokenType",
                value = "accessToken"
            )
        )

        val refreshToken = tokenService.generate(
            config = refreshTokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id.toHexString()
            ),
            TokenClaim(
                name = "tokenType",
                value = "refreshToken"
            )
        )

        return LoginResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            userId = user.id.toHexString()
        )
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