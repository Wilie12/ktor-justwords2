package example.com.words

import example.com.words.entity.BookSerializable
import kotlinx.serialization.Serializable

@Serializable
data class BooksResponse(
    val books: List<BookSerializable>
)