package cat.xlagunas.andrtc.repository;

import cat.xlagunas.andrtc.model.FriendDto;
import cat.xlagunas.andrtc.model.UserDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendRowMapper {

    public RowMapper<FriendDto> findAllRosterMapper() {
        return (rs, rowNum) -> new FriendDto.Builder()
                .id(rs.getLong(rs.findColumn("ID")))
                .email(rs.getString(rs.findColumn("EMAIL")))
                .username(rs.getString(rs.findColumn("USERNAME")))
                .firstname(rs.getString(rs.findColumn("FIRST_NAME")))
                .lastname(rs.getString(rs.findColumn("LAST_NAME")))
                .profilePic(rs.getString(rs.findColumn("PROFILE_PIC")))
                .status(FriendDto.FriendshipStatus.valueOf(rs.getString(rs.findColumn("STATUS"))))
                .build();
    }
}
