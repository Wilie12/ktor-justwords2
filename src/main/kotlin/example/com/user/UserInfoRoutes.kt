package example.com.user

import example.com.util.DataError
import example.com.util.Result
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getUserInfo(userController: UserController) {
    authenticate("accessToken") {
        get("/getUserInfo") {
            val userId = call.request.queryParameters["userId"] ?: ""

            when (val result = userController.getUserInfo(userId)) {
                is Result.Error -> {
                    when (result.error) {
                        DataError.Auth.USER_DOES_NOT_EXISTS -> {
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

fun Route.updateUserInfo(userController: UserController) {
    authenticate("accessToken") {
        post("/updateUserInfo") {
            val request = runCatching { call.receive<UserInfoRequest>() }.getOrElse {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val result = userController.updateUserInfo(
                userInfo = request.userInfo
            )

            when (result) {
                is Result.Error -> {
                    when (result.error) {
                        DataError.Auth.USER_DOES_NOT_EXISTS -> {
                            call.respond(HttpStatusCode.BadRequest, "User doesn't exists")
                        }
                        else -> {
                            call.respond(HttpStatusCode.BadRequest, "Unknown Error")
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