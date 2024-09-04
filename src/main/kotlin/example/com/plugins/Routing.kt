package example.com.plugins

import example.com.auth.AuthController
import example.com.auth.accessToken
import example.com.auth.login
import example.com.auth.register
import example.com.user.UserController
import example.com.user.getUserInfo
import example.com.user.updateUserInfo
import example.com.words.*
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
    val userController by inject<UserController>()

    routing {
        // Auth
        register(authController = authController)
        login(authController = authController)
        accessToken(authController = authController)

        // Words
        books(wordsController = wordsController)
        sets(wordsController = wordsController)
        setsById(wordsController = wordsController)
        words(wordsController = wordsController)
        getUserInfo(userController = userController)
        updateUserInfo(userController = userController)
    }
}
