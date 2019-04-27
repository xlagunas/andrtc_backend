package cat.xlagunas.andrtc.token

import com.google.common.collect.Lists
import java.util.stream.Collectors

class TokenServiceImpl(private val tokenRepository: TokenRepository) : TokenService {

    @Throws(ExistingTokenException::class)
    override fun addToken(token: Token) {
        tokenRepository.addToken(token.userId, token.value, token.platform)
    }

    override fun removeToke(token: Token) {
        tokenRepository.removeToken(token.userId, token.value)
    }

    override fun getUsersToken(userId: Long): List<String> {
        return Lists.transform(tokenRepository.getUserTokens(userId)) { token -> token!!.value }
    }

    override fun getUsersToken(userIdList: List<Long>): List<String> {
        return userIdList.stream()
                .map { this.getUsersToken(it) }
                .flatMap { it.stream() }
                .collect(Collectors.toList())
    }
}
