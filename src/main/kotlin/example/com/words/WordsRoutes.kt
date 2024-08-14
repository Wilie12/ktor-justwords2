package example.com.words

import example.com.util.Result
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.books(wordsController: WordsController) {
    authenticate("accessToken") {
        get("/books") {
            when (val result = wordsController.books()) {
                is Result.Error -> Unit
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

fun Route.sets(wordsController: WordsController) {
    authenticate("accessToken") {
        get("/sets") {
            val request = runCatching { call.receive<WordSetRequest>() }.getOrElse {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            when (val result = wordsController.setsById(request.bookId)) {
                is Result.Success -> {
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = result.data
                    )
                }
                is Result.Error -> Unit
            }
        }
    }
}

fun Route.words(wordsController: WordsController) {
    authenticate("accessToken") {
        get("/words") {
            val request = runCatching { call.receive<WordRequest>() }.getOrElse {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            when (val result = wordsController.wordsFromSet(request.setId)) {
                is Result.Success -> {
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = result.data
                    )
                }
                is Result.Error -> Unit
            }
        }
    }
}