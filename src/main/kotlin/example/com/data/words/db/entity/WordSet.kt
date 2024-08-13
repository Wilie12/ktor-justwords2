package example.com.data.words.db.entity

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class WordSet(
    val name: String,
    val bookId: String,
    val numberOfGroups: Int,
    @BsonId val id: ObjectId = ObjectId()
)
