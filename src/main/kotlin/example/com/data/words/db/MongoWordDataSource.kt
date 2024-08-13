package example.com.data.words.db

import com.mongodb.client.model.Filters
import example.com.data.words.db.entity.Book
import example.com.data.words.db.entity.Word
import example.com.data.words.db.entity.WordSet
import org.litote.kmongo.coroutine.CoroutineDatabase

class MongoWordDataSource(
    private val db: CoroutineDatabase
): WordDataSource {

    private val books = db.getCollection<Book>()
    private val sets = db.getCollection<WordSet>()
    private val words = db.getCollection<Word>()

    override suspend fun getBooks(): List<Book> {
        return books.find().toList()
    }

    override suspend fun getSets(bookId: String): List<WordSet> {
        val equalComparison = Filters.eq(WordSet::bookId.name, bookId)

        return sets.find(equalComparison).toList()
    }

    override suspend fun getWordsFromSet(setId: String): List<Word> {
        val equalComparison = Filters.eq(Word::setId.name, setId)

        return words.find(equalComparison).toList()
    }
}