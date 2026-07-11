package cat.xlagunas.andrtc.user

import java.util.Optional

interface UserRepository {

    @Throws(ExistingUserException::class)
    fun insertUser(user: User): Long

    @Throws(UserNotFoundException::class)
    fun findUser(userId: Long): User

    @Throws(UserNotFoundException::class)
    fun findUser(username: String): User

    fun findUserOptional(userId: Long): Optional<User>

    fun findUsers(username: String): List<User>

    fun updatePassword(userId: Long, password: String): Boolean

    fun updateProfilePic(userId: Long, thumbnail: String): Boolean
}
