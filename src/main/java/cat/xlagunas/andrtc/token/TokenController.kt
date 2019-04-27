package cat.xlagunas.andrtc.token

import cat.xlagunas.andrtc.common.AuthenticationUtils
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/token"])
class TokenController internal constructor(internal val tokenService: TokenService) {

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = ["/"], method = [RequestMethod.PUT])
    fun registerToken(principal: UsernamePasswordAuthenticationToken, @RequestBody token: PushTokenDto) {
        val validToken = generateValidToken(principal, token)
        try {
            tokenService.addToken(validToken)
        } catch (e: ExistingTokenException) {
            DataIntegrityViolationException("Existing token", e)
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = ["/"], method = [RequestMethod.DELETE])
    fun removeToken(principal: UsernamePasswordAuthenticationToken, @RequestBody token: PushTokenDto) {
        val validToken = generateValidToken(principal, token)
        tokenService.removeToke(validToken)
    }

    private fun generateValidToken(principal: UsernamePasswordAuthenticationToken, token: PushTokenDto): Token {
        return Token(userId = AuthenticationUtils.getPrincipalId(principal),
                value = token.value,
                platform = token.platform)
    }

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Data integrity violation")  // 409
    @ExceptionHandler(DataIntegrityViolationException::class)
    fun conflict() {
    }
}
