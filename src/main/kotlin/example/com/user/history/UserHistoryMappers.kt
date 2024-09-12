package example.com.user.history

import example.com.data.user.db.entity.UserWordHistory
import org.bson.types.ObjectId

fun UserWordHistory.toUserWordHistorySerializable(): UserWordHistorySerializable {
    return UserWordHistorySerializable(
        bookName = bookName,
        bookColor = bookColor,
        setName = setName,
        groupNumber = groupNumber,
        dateTimeUtc = dateTimeUtc,
        perfectGuessed = perfectGuessed,
        wordListSize = wordListSize,
        id = id.toHexString()
    )
}
fun UserWordHistorySerializable.toUserWordHistory(userId: String): UserWordHistory {
    return UserWordHistory(
        bookName = bookName,
        bookColor = bookColor,
        setName = setName,
        groupNumber = groupNumber,
        dateTimeUtc = dateTimeUtc,
        perfectGuessed = perfectGuessed,
        wordListSize = wordListSize,
        id = ObjectId(id),
        userId = userId
    )
}