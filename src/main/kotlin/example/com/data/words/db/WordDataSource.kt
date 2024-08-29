package example.com.data.words.db

import example.com.data.words.db.entity.Book
import example.com.data.words.db.entity.Word
import example.com.data.words.db.entity.WordSet

interface WordDataSource {

    suspend fun getBooks(): List<Book>
    suspend fun getSets(): List<WordSet>
    suspend fun getWordsFromSet(setId: String): List<Word>
}