package example.com.words.data.db

import com.mongodb.client.model.Filters
import example.com.words.data.db.entity.Book
import example.com.words.data.db.entity.Word
import example.com.words.data.db.entity.WordSet
import org.litote.kmongo.coroutine.CoroutineDatabase

class MongoWordDataSource(
    db: CoroutineDatabase
): WordDataSource {

    private val books = db.getCollection<Book>()
    private val sets = db.getCollection<WordSet>()
    private val words = db.getCollection<Word>()

    override suspend fun getBooks(): List<Book> {
        return books.find().toList()
    }

    override suspend fun getSets(): List<WordSet> {
        return sets.find().toList()
    }

    override suspend fun getWordsFromSet(setId: String): List<Word> {
        val equalComparison = Filters.eq(Word::setId.name, setId)

        return words.find(equalComparison).toList()
    }
}