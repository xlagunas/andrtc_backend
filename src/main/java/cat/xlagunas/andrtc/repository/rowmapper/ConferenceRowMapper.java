package cat.xlagunas.andrtc.repository.rowmapper;

import cat.xlagunas.andrtc.repository.model.*;
import org.springframework.jdbc.core.RowMapper;

public class ConferenceRowMapper {

    public RowMapper<Conference> findAllRosterMapper() {
        return (rs, rowNum) -> new Conference(rs.getString(rs.findColumn("ID")),
                rs.getDate(rs.findColumn("DATE")));

    }

    public RowMapper<JoinedConferenceAttendee> findAllAttendees() {
        return (rs, rowNum) ->
                new JoinedConferenceAttendee.Builder()
                        .setConferenceId(rs.getString(rs.findColumn("CONFERENCE")))
                        .setIsStarter(rs.getBoolean(rs.findColumn("STARTER")))
                        .setRoster(new JoinedRoster.Builder()
                                .id(rs.getLong("ROSTER_ID"))
                                .name(rs.getString(""))
                                .build())
                        .build();
    }
}
