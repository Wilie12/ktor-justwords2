package example.com.auth

import example.com.util.DataError
import example.com.util.Result
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.register(
    authController: AuthController
) {
    post("register") {
        val request = runCatching { call.receive<RegisterRequest>() }.getOrElse {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val result = authController.register(
            username = request.username,
            email = request.email,
            password = request.password
        )

        when (result) {
            is Result.Error -> {
                when(result.error) {
                    DataError.Auth.INVALID_CREDENTIALS -> {
                        call.respond(HttpStatusCode.BadRequest, "Invalid credentials")
                    }
                    DataError.Auth.USER_EXISTS -> {
                        call.respond(HttpStatusCode.Conflict, "User already exists")
                    }
                    else -> {
                        call.respond(HttpStatusCode.BadRequest, "Unknown Error")
                    }
                }
            }
            is Result.Success -> {
                call.respond(HttpStatusCode.OK, "User successfully registered")
            }
        }
    }
}