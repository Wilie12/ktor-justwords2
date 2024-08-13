package example.com.data.user.db

import example.com.data.user.db.entity.User

interface UserDataSource {

    suspend fun getUserByEmail(email: String): User?
    suspend fun getUserById(userId: String): User?
    suspend fun insertUser(user: User): Boolean
}