package cat.xlagunas.andrtc.repository.rowmapper;

import cat.xlagunas.andrtc.repository.model.Token;
import org.springframework.jdbc.core.RowMapper;

public class TokenRowMapper {

    private static RowMapper<Token> mapper() {
        return (rs, rowNum) -> new Token.Builder()
                .id(rs.getLong(rs.findColumn("ID")))
                .platform(rs.getString(rs.findColumn("PLATFORM")))
                .userId(rs.getLong(rs.findColumn("OWNER")))
                .value(rs.getString(rs.findColumn("TOKEN")))
                .build();

    }

    public static RowMapper<Token> insertMapper() {
        return mapper();
    }
}
