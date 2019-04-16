package cat.xlagunas.andrtc.call;

import cat.xlagunas.andrtc.roster.JoinedRoster;
import org.springframework.jdbc.core.RowMapper;

public class ConferenceRowMapper {

    public RowMapper<Conference> findAllRosterMapper() {
        return (rs, rowNum) -> new Conference(rs.getString(rs.findColumn("ID")),
                rs.getDate(rs.findColumn("DATE")));

    }

    public RowMapper<JoinedConferenceAttendee> findAllAttendees() {
        return (rs, rowNum) ->
                new JoinedConferenceAttendee.Builder()
                        .setRoster(new JoinedRoster.Builder()
                                .id(rs.getLong(rs.findColumn("ID")))
                                .email(rs.getString(rs.findColumn("EMAIL")))
                                .username(rs.getString(rs.findColumn("USERNAME")))
                                .name(rs.getString(rs.findColumn("USERNAME")))
                                .profilePic(rs.getString(rs.findColumn("PROFILE_PIC")))
                                .build())
                        .setIsStarter(rs.getBoolean(rs.findColumn("STARTER")))
                        .build();
    }
}
