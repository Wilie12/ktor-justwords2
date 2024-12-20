package example.com.user.data.token

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

class JWTTokenService: TokenService {

    override fun generate(config: TokenConfig, vararg claims: TokenClaim): String {
        var token = JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withExpiresAt(Date(System.currentTimeMillis() + config.expiresIn))

        claims.forEach { claim ->
            token = token.withClaim(claim.name, claim.value)
        }

        return token.sign(Algorithm.HMAC256(config.secret))
    }

    override fun verify(config: TokenConfig, tokenType: String): JWTVerifier {
        return JWT
            .require(Algorithm.HMAC256(System.getenv("JWT_SECRET")))
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withClaim("tokenType", tokenType)
            .build()
    }
}