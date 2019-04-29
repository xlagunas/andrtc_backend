package cat.xlagunas.andrtc.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.transaction.annotation.Transactional

import com.google.common.base.Function
import com.google.common.collect.Lists

@Transactional
class UserServiceImpl(private var userRepository: UserRepository) : UserService {

    @Throws(UserNotFoundException::class)
    override fun findUser(idUser: Long): UserDto {
        return UserConverter.convert(userRepository.findUser(idUser))
    }

    @Throws(UserNotFoundException::class)
    override fun findUser(username: String): UserDto {
        return UserConverter.convert(userRepository.findUser(username))
    }

    override fun searchByUsername(username: String): List<UserDto> {
        return Lists.transform(userRepository.findUsers(username)) { user -> UserConverter.convert(user!!) }
    }

    @Throws(ExistingUserException::class)
    override fun createUser(userDto: UserDto) {
        userRepository.insertUser(UserConverter.convert(userDto))
    }

    override fun updatePassword(userId: Long, password: String): Boolean {
        return userRepository.updatePassword(userId, password)
    }

    override fun updateProfilePicture(userId: Long, profileUrl: String): Boolean {
        return userRepository.updateProfilePic(userId, profileUrl)
    }

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        try {
            return findUser(username)
        } catch (e: UserNotFoundException) {
            throw UsernameNotFoundException(e.message, e)
        }

    }
}
