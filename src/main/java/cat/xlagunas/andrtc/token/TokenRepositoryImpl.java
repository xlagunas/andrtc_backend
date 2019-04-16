package cat.xlagunas.andrtc.token;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;

public class TokenRepositoryImpl implements TokenRepository {
    private final static String INSERT_TOKEN = "INSERT INTO TOKEN (OWNER, TOKEN, PLATFORM) VALUES (:owner, :token, :platform)";
    private final static String FIND_USER_TOKENS = "SELECT * FROM TOKEN WHERE TOKEN.OWNER = :owner";
    private final static String FIND_LIST_USER_TOKENS = "SELECT * FROM TOKEN WHERE TOKEN.OWNER IN (:ownerList)";
    private final static String DELETE_TOKEN = "DELETE FROM TOKEN WHERE TOKEN.OWNER = :owner AND TOKEN.TOKEN = :token";

    NamedParameterJdbcTemplate jdbcTemplate;

    public TokenRepositoryImpl(NamedParameterJdbcTemplate template) {
        this.jdbcTemplate = template;
    }

    @Override
    public long addToken(long userId, String token, String platform) throws ExistingTokenException {
        try {
            KeyHolder holder = new GeneratedKeyHolder();
            long id = jdbcTemplate.update(INSERT_TOKEN,
                    new MapSqlParameterSource("owner", userId)
                            .addValue("token", token)
                            .addValue("platform", platform),
                    holder);
            return holder.getKey().longValue();
        } catch (DuplicateKeyException ex) {
            throw new ExistingTokenException(ex);
        }
    }

    @Override
    public List<Token> getUserTokens(long userId) {
        return jdbcTemplate.query(FIND_USER_TOKENS, new MapSqlParameterSource("owner", userId), TokenRowMapper.insertMapper());
    }

    public List<Token> getUserListToken(List<Long> userId) {
        return jdbcTemplate.query(FIND_LIST_USER_TOKENS, new MapSqlParameterSource("ownerList", userId), TokenRowMapper.insertMapper());
    }

    @Override
    public boolean removeToken(long userId, String token) {
        int affectedRows = jdbcTemplate.update(DELETE_TOKEN,
                new MapSqlParameterSource("owner", userId)
                        .addValue("token", token));
        return affectedRows > 0;
    }
}
