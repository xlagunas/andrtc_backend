package cat.xlagunas.andrtc.user

import org.springframework.security.core.userdetails.UserDetailsService

interface UserService : UserDetailsService {

    @Throws(UserNotFoundException::class)
    fun findUser(idUser: Long): UserDto

    @Throws(UserNotFoundException::class)
    fun findUser(username: String): UserDto

    fun searchByUsername(username: String): List<UserDto>

    @Throws(ExistingUserException::class)
    fun createUser(userDto: UserDto)

    fun updatePassword(userId: Long, password: String): Boolean

    fun updateProfilePicture(userId: Long, profileUrl: String): Boolean

}
