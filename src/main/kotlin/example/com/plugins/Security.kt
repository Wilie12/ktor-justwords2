package example.com.plugins

import example.com.data.user.token.TokenConfig
import example.com.data.user.token.TokenService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {

    val tokenConfig by inject<TokenConfig>(named("accessToken"))
    val tokenService by inject<TokenService>()

    val jwtAudience = "users"
    val jwtRealm = "ktor-justwords2-server"
    authentication {
        jwt("accessToken") {
            realm = jwtRealm
            verifier(tokenService.verify(tokenConfig, "accessToken"))
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
            }
        }
        jwt("refreshToken") {
            realm = jwtRealm
            verifier(tokenService.verify(tokenConfig, "refreshToken"))
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
            }
        }
    }
}
