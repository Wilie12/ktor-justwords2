package example.com.words

import kotlinx.serialization.Serializable

@Serializable
data class BooksResponse(
    val books: List<BookSerializable>
)