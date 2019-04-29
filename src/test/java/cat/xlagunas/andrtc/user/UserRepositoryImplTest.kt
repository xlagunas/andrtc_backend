package cat.xlagunas.andrtc.user

import cat.xlagunas.andrtc.common.UserRowMapper

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

import java.util.Optional

import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

@RunWith(SpringRunner::class)
@SpringBootTest
class UserRepositoryImplTest {

    @Autowired
    internal var template: JdbcTemplate? = null

    private lateinit var userRepository: UserRepository

    private lateinit var user: User

    @Before
    fun setUp() {
        user = UserTestBuilder.user
        userRepository = UserRepositoryImpl(template!!, UserRowMapper(), NoOpPasswordEncoder.getInstance())
    }

    @Test
    @Transactional
    @Rollback
    @Throws(ExistingUserException::class)
    fun whenInsert_thenUserPersisted() {
        val id = userRepository.insertUser(user)

        val User = userRepository.findUserOptional(id)
        assertThat(User.isPresent)
        assertThat(User.get()).isEqualToIgnoringGivenFields(user, "id", "modifiedDate")
    }

    @Test(expected = ExistingUserException::class)
    @Rollback
    @Transactional
    @Throws(ExistingUserException::class)
    fun givenUsernameAlreadyUsed_whenInsertingUser_thenThrowException() {
        userRepository.insertUser(user)
        userRepository.insertUser(user)
    }

    @Test
    @Rollback
    @Transactional
    @Throws(ExistingUserException::class)
    fun givenValidUser_whenUpdatePassword_thenUserUpdated() {
        val id = userRepository.insertUser(user)

        val state = userRepository.updatePassword(id, "newPass")

        assertTrue(state)
    }

    @Test
    @Rollback
    @Transactional
    fun givenNonValidUser_whenUpdatePassword_thenFailed() {
        val state = userRepository.updatePassword(100, "newPass")

        assertFalse(state)
    }

    @Test
    @Rollback
    @Transactional
    @Throws(ExistingUserException::class)
    fun whenUpdateProfilePic_thenUserUpdated() {
        val id = userRepository.insertUser(user)

        val state = userRepository
                .updateProfilePic(id, "https://google.com/aPic1")

        assertTrue(state)
    }

}