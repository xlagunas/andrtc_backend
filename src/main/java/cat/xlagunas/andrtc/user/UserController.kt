package cat.xlagunas.andrtc.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

import cat.xlagunas.andrtc.common.AuthenticationUtils

@RestController
@RequestMapping(value = ["/user"])
class UserController(@field:Autowired
                     private val userService: UserService) {

    @Value("\${jwt.header}")
    private val tokenHeader: String? = null

    @RequestMapping(value = ["/"], method = [RequestMethod.PUT])
    @ResponseStatus(HttpStatus.CREATED)
    @Throws(ExistingUserException::class)
    internal fun createUser(@RequestBody userDto: UserDto) {
        userService.createUser(userDto)
    }

    @RequestMapping(value = ["/"], method = [RequestMethod.GET])
    @Throws(UserNotFoundException::class)
    internal fun getUser(principal: UsernamePasswordAuthenticationToken): UserDto {
        val principalId = AuthenticationUtils.getPrincipalId(principal)
        return userService.findUser(principalId)
    }

    @RequestMapping(value = ["/password"], method = [RequestMethod.POST])
    @ResponseStatus(HttpStatus.OK)
    internal fun changePassword(principal: UsernamePasswordAuthenticationToken, @RequestBody newUserData: UserDto) {
        val principalId = AuthenticationUtils.getPrincipalId(principal)
        if (userService.updatePassword(principalId, newUserData.password)) {
            return
        } else {
            throw DataIntegrityViolationException("Request could not be performed")
        }
    }

    @RequestMapping(value = ["/thumbnail"], method = [RequestMethod.POST])
    @ResponseStatus(HttpStatus.OK)
    internal fun updateProfilePicture(principal: UsernamePasswordAuthenticationToken, @RequestBody newUserData: UserDto) {
        val principalId = AuthenticationUtils.getPrincipalId(principal)
        if (userService.updateProfilePicture(principalId, newUserData.profilePic)) {
            return
        } else {
            throw DataIntegrityViolationException("Request could not be performed")
        }
    }

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Data integrity violation")  // 409
    @ExceptionHandler(DataIntegrityViolationException::class)
    fun conflict() {
    }

}
