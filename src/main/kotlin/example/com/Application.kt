package example.com

import example.com.auth.AuthController
import example.com.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.ext.inject

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val authController by inject<AuthController>()

    configureDependencyInjection()
    configureSerialization()
    configureSecurity()
    configureRouting(authController)
}
