package example.com.di

import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val appModule = module {

    single<CoroutineDatabase> {
        KMongo.createClient(
            connectionString = "mongodb://localhost:27017/ktor-justwords2"
        )
            .coroutine
            .getDatabase("ktor-justwords2")
    }
}