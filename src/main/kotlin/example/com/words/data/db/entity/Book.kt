package example.com.words.data.db.entity

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Book(
    val name: String,
    val color: Int,
    @BsonId val id: ObjectId = ObjectId()
)
