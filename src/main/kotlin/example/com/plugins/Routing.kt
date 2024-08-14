package example.com.plugins

import example.com.auth.AuthController
import example.com.auth.accessToken
import example.com.auth.login
import example.com.auth.register
import example.com.words.WordsController
import example.com.words.books
import example.com.words.sets
import example.com.words.words
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
        }
    }

    val authController by inject<AuthController>()
    val wordsController by inject<WordsController>()

    routing {
        // Auth
        register(authController = authController)
        login(authController = authController)
        accessToken(authController = authController)

        // Words
        books(wordsController = wordsController)
        sets(wordsController = wordsController)
        words(wordsController = wordsController)
    }
}
