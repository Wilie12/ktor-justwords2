package example.com.data.user.db

interface UserDataSource {

    suspend fun getUserByEmail(email: String): User?
    suspend fun insertUser(user: User): Boolean
}