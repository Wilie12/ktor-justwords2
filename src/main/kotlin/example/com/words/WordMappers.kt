package example.com.words

import example.com.words.data.db.entity.Book
import example.com.words.data.db.entity.Word
import example.com.words.data.db.entity.WordSet
import example.com.words.model.BookSerializable
import example.com.words.model.WordSerializable
import example.com.words.model.WordSetSerializable

fun Book.toBookSerializable(): BookSerializable {
    return BookSerializable(
        name = name,
        color = color,
        id = id.toHexString()
    )
}

fun WordSet.toWordSetSerializable(): WordSetSerializable {
    return WordSetSerializable(
        name = name,
        bookId = bookId,
        numberOfGroups = numberOfGroups,
        id = id.toHexString()
    )
}

fun Word.toWordSerializable(): WordSerializable {
    return WordSerializable(
        sentence = sentence,
        wordPl = wordPl,
        wordEng = wordEng,
        setId = setId,
        groupNumber = groupNumber,
        id = id.toHexString()
    )
}