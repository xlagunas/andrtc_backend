package cat.xlagunas.andrtc.token

interface TokenService {

    @Throws(ExistingTokenException::class)
    fun addToken(token: Token)

    fun removeToke(token: Token)

    fun getUsersToken(userId: Long): List<String>

    fun getUsersToken(userIdList: List<Long>): List<String>
}
