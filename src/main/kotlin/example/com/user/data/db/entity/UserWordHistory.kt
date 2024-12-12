package example.com.user.data.db.entity

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class UserWordHistory(
    val bookName: String,
    val bookColor: Int,
    val setName: String,
    val groupNumber: Int,
    val dateTimeUtc: String,
    val perfectGuessed: Int,
    val wordListSize: Int,
    val userId: String,
    @BsonId val id: ObjectId = ObjectId()
)
