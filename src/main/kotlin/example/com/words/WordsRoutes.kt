package example.com.words

import example.com.util.Result
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
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
            when (val result = wordsController.sets()) {
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

fun Route.setsById(wordsController: WordsController) {
    authenticate("accessToken") {
        get("/setsById") {
            when (val result = wordsController.sets()) {
                is Result.Error -> Unit
                is Result.Success -> {

                    val sets = result.data.sets
                        .filter { it.bookId == call.request.queryParameters["bookId"] }

                    call.respond(
                        status = HttpStatusCode.OK,
                        message = WordSetResponse(
                            sets = sets
                        )
                    )
                }
            }
        }
    }
}

fun Route.words(wordsController: WordsController) {
    authenticate("accessToken") {
        get("/words") {
            val setId = call.request.queryParameters["setId"] ?: ""

            when (val result = wordsController.wordsFromSet(setId)) {
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