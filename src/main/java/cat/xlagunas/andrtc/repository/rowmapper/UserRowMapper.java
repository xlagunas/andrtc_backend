package cat.xlagunas.andrtc.repository.rowmapper;

import cat.xlagunas.andrtc.repository.model.User;
import org.springframework.jdbc.core.RowMapper;

public class UserRowMapper {

    public RowMapper<User> searchUsersRowMapper = fullUserRowMapper();

    public RowMapper<User> insertUserRowMapper = fullUserRowMapper();

    private static RowMapper<User> fullUserRowMapper() {
        return (rs, rowNum) -> new User.Builder()
                .id(rs.getLong(rs.findColumn("ID")))
                .email(rs.getString(rs.findColumn("EMAIL")))
                .username(rs.getString(rs.findColumn("USERNAME")))
                .firstname(rs.getString(rs.findColumn("FIRST_NAME")))
                .lastname(rs.getString(rs.findColumn("LAST_NAME")))
                .password(rs.getString(rs.findColumn("PASSWORD")))
                .profilePic(rs.getString(rs.findColumn("PROFILE_PIC")))
                .modifiedDate(rs.getTimestamp(rs.findColumn("LAST_PASSWORD_UPDATE")).toInstant())
                .build();
    }

}
