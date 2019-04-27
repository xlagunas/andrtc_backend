package cat.xlagunas.andrtc.token

interface TokenRepository {

    @Throws(ExistingTokenException::class)
    fun addToken(userId: Long, token: String, platform: String): Long

    fun getUserTokens(userId: Long): List<Token>

    fun getUserListToken(userId: List<Long>): List<Token>

    fun removeToken(userId: Long, token: String): Boolean
}
