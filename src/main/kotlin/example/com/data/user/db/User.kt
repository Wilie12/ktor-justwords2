package example.com.data.user.db

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    val username: String,
    val email: String,
    val password: String,
    val sat: String,
    @BsonId val id: ObjectId = ObjectId()
)
