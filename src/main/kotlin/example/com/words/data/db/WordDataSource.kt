package example.com.words.data.db

import example.com.words.data.db.entity.Book
import example.com.words.data.db.entity.Word
import example.com.words.data.db.entity.WordSet

interface WordDataSource {

    suspend fun getBooks(): List<Book>
    suspend fun getSets(): List<WordSet>
    suspend fun getWordsFromSet(setId: String): List<Word>
}