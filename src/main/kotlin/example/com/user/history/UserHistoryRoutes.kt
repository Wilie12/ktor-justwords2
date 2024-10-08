package example.com.user.history

import example.com.util.DataError
import example.com.util.Result
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userHistory(userHistoryController: UserHistoryController) {
    authenticate("accessToken") {
        get("/userHistory") {
            val principal = call.principal<JWTPrincipal>() ?: return@get
            val userId = principal.payload.getClaim("userId").asString()

            when (val result = userHistoryController.getAllUserHistoryById(userId)) {
                is Result.Error -> {
                    when (result.error) {
                        DataError.Insert.USER_DOES_NOT_EXISTS -> {
                            call.respond(HttpStatusCode.BadRequest, "User doesn't exists")
                        }
                        else -> {
                            call.respond(HttpStatusCode.BadRequest, "Unknown error")
                        }
                    }
                }
                is Result.Success -> {
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = result.data
                    )
                }
            }
        }
    }
}

fun Route.postHistory(userHistoryController: UserHistoryController) {
    authenticate("accessToken") {
        post("/postUserHistory") {
            val request = runCatching { call.receive<UserWordHistorySerializable>() }.getOrElse {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val principal = call.principal<JWTPrincipal>() ?: return@post
            val userId = principal.payload.getClaim("userId").asString()
            val userWordHistory = request.toUserWordHistory(userId)

            when (val result = userHistoryController.insertUserHistory(userWordHistory)) {
                is Result.Error -> {
                    when (result.error) {
                        DataError.Insert.USER_DOES_NOT_EXISTS -> {
                            call.respond(HttpStatusCode.Unauthorized, "User doesn't exists")
                        }
                        DataError.Insert.COULD_NOT_INSERT -> {
                            call.respond(HttpStatusCode.Conflict, "Couldn't insert history")
                        }
                    }
                }
                is Result.Success -> {
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}