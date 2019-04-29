package cat.xlagunas.andrtc.common

import cat.xlagunas.andrtc.user.UserDto
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

object AuthenticationUtils {
    fun getPrincipalId(principal: UsernamePasswordAuthenticationToken): Long {
        val dto = principal.principal as UserDto
        return dto.id
    }
}
