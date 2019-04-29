package cat.xlagunas.andrtc.common

import cat.xlagunas.andrtc.user.User
import org.springframework.jdbc.core.RowMapper

class UserRowMapper {

    var searchUsersRowMapper = fullUserRowMapper()

    var insertUserRowMapper = fullUserRowMapper()

    private fun fullUserRowMapper(): RowMapper<User> {
        return RowMapper{ rs, rowNum ->
            User.Builder()
                    .id(rs.getLong(rs.findColumn("ID")))
                    .email(rs.getString(rs.findColumn("EMAIL")))
                    .username(rs.getString(rs.findColumn("USERNAME")))
                    .firstname(rs.getString(rs.findColumn("FIRST_NAME")))
                    .lastname(rs.getString(rs.findColumn("LAST_NAME")))
                    .password(rs.getString(rs.findColumn("PASSWORD")))
                    .profilePic(rs.getString(rs.findColumn("PROFILE_PIC")))
                    .modifiedDate(rs.getTimestamp(rs.findColumn("LAST_PASSWORD_UPDATE")).toInstant())
                    .build()
        }
    }

}
