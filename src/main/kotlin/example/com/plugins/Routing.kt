package example.com.plugins

import example.com.auth.AuthController
import example.com.auth.accessToken
import example.com.auth.login
import example.com.auth.register
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(authController: AuthController) {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
        }
    }
    routing {
        register(authController = authController)
        login(authController = authController)
        accessToken(authController = authController)
    }
}
