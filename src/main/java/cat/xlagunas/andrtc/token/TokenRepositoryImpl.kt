package cat.xlagunas.andrtc.token

import org.springframework.dao.DuplicateKeyException
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder

class TokenRepositoryImpl(internal var jdbcTemplate: NamedParameterJdbcTemplate) : TokenRepository {

    @Throws(ExistingTokenException::class)
    override fun addToken(userId: Long, token: String, platform: String): Long {
        try {
            val queryParams = MapSqlParameterSource()
                    .addValue("owner", userId)
                    .addValue("token", token)
                    .addValue("platform", platform)

            return jdbcTemplate.update(INSERT_TOKEN, queryParams, GeneratedKeyHolder()).toLong()
        } catch (ex: DuplicateKeyException) {
            throw ExistingTokenException(ex)
        }
    }

    override fun getUserTokens(userId: Long): List<Token> {
        val queryParams = MapSqlParameterSource("owner", userId)
        return jdbcTemplate.query(FIND_USER_TOKENS, queryParams, TokenRowMapper.insertMapper())
    }

    override fun getUserListToken(userId: List<Long>): List<Token> {
        val queryParams = MapSqlParameterSource("ownerList", userId)
        return jdbcTemplate.query(FIND_LIST_USER_TOKENS, queryParams, TokenRowMapper.insertMapper())
    }

    override fun removeToken(userId: Long, token: String): Boolean {
        val queryParams = MapSqlParameterSource()
                .addValue("owner", userId)
                .addValue("token", token)
        return jdbcTemplate.update(DELETE_TOKEN, queryParams) > 0
    }

    companion object {
        private val INSERT_TOKEN = "INSERT INTO TOKEN (OWNER, TOKEN, PLATFORM) VALUES (:owner, :token, :platform)"
        private val FIND_USER_TOKENS = "SELECT * FROM TOKEN WHERE TOKEN.OWNER = :owner"
        private val FIND_LIST_USER_TOKENS = "SELECT * FROM TOKEN WHERE TOKEN.OWNER IN (:ownerList)"
        private val DELETE_TOKEN = "DELETE FROM TOKEN WHERE TOKEN.OWNER = :owner AND TOKEN.TOKEN = :token"
    }
}
