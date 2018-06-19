package cat.xlagunas.andrtc.repository.rowmapper;

import cat.xlagunas.andrtc.repository.model.JoinedRoster;
import cat.xlagunas.andrtc.repository.model.Roster;
import org.springframework.jdbc.core.RowMapper;

public class RosterRowMapper {

    public RowMapper<JoinedRoster> findAllRosterMapper() {
        return (rs, rowNum) -> new JoinedRoster.Builder()
                .id(rs.getLong(rs.findColumn("ID")))
                .email(rs.getString(rs.findColumn("EMAIL")))
                .username(rs.getString(rs.findColumn("USERNAME")))
                .name(rs.getString(rs.findColumn("NAME")))
                .profilePic(rs.getString(rs.findColumn("PROFILE_PIC")))
                .status(rs.getString(rs.findColumn("STATUS")))
                .build();
    }

    public RowMapper<JoinedRoster> searchContact() {
        return (rs, rowNum) -> new JoinedRoster.Builder()
                .id(rs.getLong(rs.findColumn("ID")))
                .email(rs.getString(rs.findColumn("EMAIL")))
                .username(rs.getString(rs.findColumn("USERNAME")))
                .name(rs.getString(rs.findColumn("NAME")))
                .profilePic(rs.getString(rs.findColumn("PROFILE_PIC")))
                .status(rs.getString(rs.findColumn("STATUS")))
                .build();
    }

    public RowMapper<Roster> getRoster() {
        return (rs, rowNum) -> new Roster.Builder()
                .id(rs.getLong(rs.findColumn("ID")))
                .owner(rs.getLong(rs.findColumn("OWNER")))
                .contact(rs.getLong(rs.findColumn("CONTACT")))
                .relationStatus(rs.getString(rs.findColumn("STATUS")))
                .build();
    }

    public RowMapper<Long> getRelationshipIds() {
        return (rs, rowNum) -> rs.getLong(rs.findColumn("ID"));
    }
}
