package example.com.words

import example.com.data.words.db.WordDataSource
import example.com.util.Error
import example.com.util.Result

class WordsController(
    private val wordDataSource: WordDataSource
) {

    suspend fun books(): Result<BooksResponse, Error> {
        val books = wordDataSource.getBooks()
        return Result.Success(
            BooksResponse(
                books = books.map { it.toBookSerializable() }
            )
        )
    }

    suspend fun sets(): Result<WordSetResponse, Error> {
        val sets = wordDataSource.getSets()
        return Result.Success(
            WordSetResponse(
                sets = sets.map { it.toWordSetSerializable() }
            )
        )
    }

    suspend fun wordsFromSet(setId: String): Result<WordsResponse, Error> {
        val words = wordDataSource.getWordsFromSet(setId)
        return Result.Success(
            WordsResponse(
                words = words.map { it.toWordSerializable() }
            )
        )
    }
}