package example.com.user.data.db

import example.com.user.data.db.entity.User
import example.com.user.data.db.entity.UserInfo

interface UserDataSource {

    suspend fun getUserByEmail(email: String): User?
    suspend fun getUserById(userId: String): User?
    suspend fun insertUser(user: User, username: String): Boolean
    suspend fun getUserInfo(userId: String): UserInfo?
    suspend fun updateUserInfo(userInfo: UserInfo): Boolean
}