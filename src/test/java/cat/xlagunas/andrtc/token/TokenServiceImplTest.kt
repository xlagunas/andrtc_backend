package cat.xlagunas.andrtc.token

import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.*

class TokenServiceImplTest {
    @Mock
    private lateinit var tokenRepository: TokenRepository
    private lateinit var tokenService: TokenService

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        tokenService = TokenServiceImpl(tokenRepository)
    }

    @Test
    @Throws(ExistingTokenException::class)
    fun whenAddingNewToken_thenNewTokenValid() {
        tokenService.addToken(token)

        verify<TokenRepository>(tokenRepository).addToken(USER_ID.toLong(), VALUE, PLATFORM)
    }

    @Test
    @Throws(Exception::class)
    fun whenRemovingNewToken_thenDeleted() {
        tokenService.removeToke(token)

        verify<TokenRepository>(tokenRepository).removeToken(USER_ID.toLong(), VALUE)
    }

    @Test
    @Throws(Exception::class)
    fun getUserToken() {
        `when`(tokenRepository.getUserTokens(USER_ID.toLong())).thenReturn(Arrays.asList(token))

        val tokenList = tokenService.getUsersToken(USER_ID.toLong())

        assertThat(tokenList[0]).contains(VALUE)
    }

    companion object {

        private val USER_ID = 10
        private val VALUE = "ASDFG1234512"
        private val PLATFORM = "ANDROID"

        private val token: Token
            get() = Token(0, USER_ID.toLong(), VALUE, PLATFORM)
    }

}