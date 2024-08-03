package example.com.data.user.token

import com.auth0.jwt.JWTVerifier

interface TokenService {

    fun generate(config: TokenConfig, vararg claims: TokenClaim): String
    fun verify(config: TokenConfig, tokenType: String): JWTVerifier
}