package example.com.di

import example.com.auth.AuthController
import example.com.user.data.db.MongoUserDataSource
import example.com.user.data.db.UserDataSource
import example.com.user.data.db.UserHistoryDataSource
import example.com.user.data.db.entity.MongoUserHistoryDataSource
import example.com.user.data.hashing.HashingService
import example.com.user.data.hashing.SHA256HashingService
import example.com.user.data.token.JWTTokenService
import example.com.user.data.token.TokenConfig
import example.com.user.data.token.TokenService
import example.com.user.data.token.refresh.MongoRefreshTokenRepository
import example.com.user.data.token.refresh.RefreshTokenRepository
import example.com.words.data.db.MongoWordDataSource
import example.com.words.data.db.WordDataSource
import example.com.user.UserController
import example.com.user.history.UserHistoryController
import example.com.words.WordsController
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val appModule = module {

    single<TokenConfig>(named("accessToken")) {
        TokenConfig(
            issuer = "http://127.0.0.1:8080",
            audience = "users",
            expiresIn = 1000L * 60L * 60,
            secret = System.getenv("JWT_SECRET")
        )
    }
    single<TokenConfig>(named("refreshToken")) {
        TokenConfig(
            issuer = "http://127.0.0.1:8080",
            audience = "users",
            expiresIn = 1000L * 60L * 60L * 24L * 365L,
            secret = System.getenv("JWT_SECRET")
        )
    }

    single<CoroutineDatabase> {
        KMongo.createClient(
            connectionString = "mongodb://localhost:27017/ktor-justwords2"
        )
            .coroutine
            .getDatabase("ktor-justwords2")
    }

    single<AuthController> {
        AuthController(
            userDataSource = get(),
            hashingService = get(),
            tokenService = get(),
            accessTokenConfig = get(named("accessToken")),
            refreshTokenConfig = get(named("refreshToken")),
            refreshTokenRepository = get()
        )
    }

    singleOf(::MongoRefreshTokenRepository).bind<RefreshTokenRepository>()
    singleOf(::MongoUserDataSource).bind<UserDataSource>()
    singleOf(::SHA256HashingService).bind<HashingService>()
    singleOf(::JWTTokenService).bind<TokenService>()

    singleOf(::MongoWordDataSource).bind<WordDataSource>()
    singleOf(::WordsController)

    singleOf(::UserController)
    singleOf(::MongoUserHistoryDataSource).bind<UserHistoryDataSource>()
    singleOf(::UserHistoryController)
}