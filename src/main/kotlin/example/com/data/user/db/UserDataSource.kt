package example.com.data.user.db

import example.com.data.user.db.entity.User
import example.com.data.user.db.entity.UserInfo

interface UserDataSource {

    suspend fun getUserByEmail(email: String): User?
    suspend fun getUserById(userId: String): User?
    suspend fun insertUser(user: User): Boolean
    suspend fun getUserInfo(userId: String): UserInfo?
    suspend fun updateUserInfo(userInfo: UserInfo): Boolean
}