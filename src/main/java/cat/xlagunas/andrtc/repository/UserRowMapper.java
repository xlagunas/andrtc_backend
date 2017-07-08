package cat.xlagunas.andrtc.repository;

import cat.xlagunas.andrtc.model.UserDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper {

    public RowMapper<UserDto> searchUsersRowMapper = fullUserRowMapper();

    public RowMapper<UserDto> insertUserRowMapper = fullUserRowMapper();

    private static RowMapper<UserDto> fullUserRowMapper() {
        return (rs, rowNum) -> new UserDto.Builder()
                .id(rs.getLong(rs.findColumn("ID")))
                .email(rs.getString(rs.findColumn("EMAIL")))
                .username(rs.getString(rs.findColumn("USERNAME")))
                .firstname(rs.getString(rs.findColumn("FIRST_NAME")))
                .lastname(rs.getString(rs.findColumn("LAST_NAME")))
                .password(rs.getString(rs.findColumn("PASSWORD")))
                .profilePic(rs.getString(rs.findColumn("PROFILE_PIC")))
                .build();
    }

}
