package example.com.words.data.db.entity

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Word(
    val sentence: String,
    val wordPl: String,
    val wordEng: String,
    val setId: String,
    val groupNumber: Int,
    @BsonId val id: ObjectId = ObjectId()
)
