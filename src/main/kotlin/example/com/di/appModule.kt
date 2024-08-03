package example.com.di

import example.com.data.user.db.MongoDataSource
import example.com.data.user.db.UserDataSource
import example.com.data.user.hashing.HashingService
import example.com.data.user.hashing.SHA256HashingService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
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

    singleOf(::MongoDataSource).bind<UserDataSource>()
    singleOf(::SHA256HashingService).bind<HashingService>()
}