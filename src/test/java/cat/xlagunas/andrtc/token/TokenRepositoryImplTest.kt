package cat.xlagunas.andrtc.token

import cat.xlagunas.andrtc.user.ExistingUserException
import cat.xlagunas.andrtc.user.User
import cat.xlagunas.andrtc.user.UserRepository
import com.google.common.collect.Lists
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

@RunWith(SpringRunner::class)
@SpringBootTest
class TokenRepositoryImplTest {

    @Autowired
    private lateinit var tokenRepository: TokenRepository

    @Autowired
    private lateinit var userRepositoryImpl: UserRepository

    @Autowired
    internal var encoder: PasswordEncoder? = null

    internal var userId: Long = 0

    @Before
    fun setup() {
        try {
            userId = userRepositoryImpl.insertUser(User.Builder().username("aUsername").password("aPassword").email("anEmail").firstname("aFirstName").build())
        } catch (e: ExistingUserException) {
            e.printStackTrace()
        }

    }

    @Test
    @Transactional
    @Rollback
    @Throws(ExistingTokenException::class)
    fun addToken() {
        val token = generateFakeToken(userId, "aToken")

        tokenRepository.addToken(token.userId, token.value, token.platform)

        val tokens = tokenRepository.getUserTokens(userId)
        assertThat(tokens).hasSize(1)
        assertThat(tokens[0]).isEqualToIgnoringGivenFields(token, "id")
    }

    @Test
    @Transactional
    @Rollback
    @Throws(ExistingTokenException::class)
    fun getUserTokens() {
        for ((_, userId1, value, platform) in generateFakeTokenList(userId)) {
            tokenRepository.addToken(userId1, value, platform)
        }

        val tokens = tokenRepository.getUserTokens(userId)

        assertThat(tokens).hasSize(4)
    }

    @Test
    @Transactional
    @Rollback
    @Throws(ExistingTokenException::class)
    fun removeToken() {
        for ((_, userId1, value, platform) in generateFakeTokenList(userId)) {
            tokenRepository.addToken(userId1, value, platform)
        }

        val tokens = tokenRepository.getUserTokens(userId)
        assertThat(tokens).hasSize(4)
        assertThat(tokenRepository.removeToken(userId, "aToken")).isTrue()
        assertThat(tokenRepository.getUserTokens(userId)).hasSize(3)
    }

    private fun generateFakeToken(userId: Long, value: String): Token {
        return Token(0, userId, value, "ANDROID")
    }

    private fun generateFakeTokenList(userId: Long): List<Token> {
        return Lists.newArrayList(generateFakeToken(userId, "aToken"),
                generateFakeToken(userId, "anotherToken"),
                generateFakeToken(userId, "anotherToken2"),
                generateFakeToken(userId, "anothertoken3"))
    }

}