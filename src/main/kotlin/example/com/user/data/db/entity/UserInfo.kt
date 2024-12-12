package example.com.user.data.db.entity

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class UserInfo(
    val dailyStreak: Int,
    val bestStreak: Int,
    val currentGoal: Int,
    val dailyGoal: Int,
    val lastPlayedTimestamp: String,
    val lastEditedTimestamp: String,
    val username: String,
    @BsonId val userId: ObjectId = ObjectId()
)
