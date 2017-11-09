package cat.xlagunas.andrtc.repository;

import cat.xlagunas.andrtc.repository.model.Conference;
import cat.xlagunas.andrtc.repository.model.JoinedConferenceAttendee;
import cat.xlagunas.andrtc.repository.rowmapper.ConferenceRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class CallRepositoryImpl implements CallRepository {

    private final static String CREATE_CONFERENCE = "INSERT INTO CONFERENCE (ID, DATE) VALUES (:callId, :date)";
    private final static String FIND_CONFERENCE = "SELECT * FROM CONFERENCE WHERE ID = :callId";
    private final static String JOIN_USER = "INSERT INTO CONFERENCE_ATTENDEE (CONFERENCE, PARTICIPANT, STARTER, STATUS) " +
            "VALUES (:callId, :userId, :isStarter, :status)";
    private final static String GET_CONFERENCE_USERS = "SELECT USER.ID, EMAIL, USERNAME, CONCAT_WS(' ', FIRST_NAME, LAST_NAME) AS NAME, " +
            "PROFILE_PIC, CONFERENCE_ATTENDEE.STARTER FROM CONFERENCE_ATTENDEE LEFT JOIN USER ON USER.ID = CONFERENCE_ATTENDEE.PARTICIPANT " +
            "WHERE CONFERENCE = :callId AND NOT CONFERENCE_ATTENDEE.PARTICIPANT=:userId";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ConferenceRowMapper rowMapper;

    public CallRepositoryImpl(NamedParameterJdbcTemplate template, ConferenceRowMapper rowMapper) {
        this.jdbcTemplate = template;
        this.rowMapper = rowMapper;
    }

    @Override
    public Conference createCall() {
        Conference conference = new Conference(UUID.randomUUID().toString(),
                Date.from(Instant.now()));

        jdbcTemplate.update(CREATE_CONFERENCE,
                new MapSqlParameterSource("callId", conference.callId)
                        .addValue("date", conference.date));

//        jdbcTemplate.update(JOIN_USER, new MapSqlParameterSource("callId", conference.callId)
//                .addValue("userId", userId)
//                .addValue("isStarter", true)
//                .addValue("status", Status.ACCEPTED));
//
//        for (long contactId : contacts) {
//            jdbcTemplate.update(JOIN_USER, new MapSqlParameterSource("callId", conference.callId)
//                    .addValue("userId", contactId)
//                    .addValue("isStarter", false)
//                    .addValue("status", Status.UNATTENDED));
//        }

        return conference;
    }

    @Override
    public void updateCallContact(String callId, long userId, boolean isStarter, Status status) {
        jdbcTemplate.update(JOIN_USER, new MapSqlParameterSource()
                .addValue("callId", callId)
                .addValue("userId", userId)
                .addValue("isStarter", isStarter)
                .addValue("status", Status.ACCEPTED));
    }

    @Override
    public Conference getConferenceById(String callId) {
        Conference conference = jdbcTemplate.queryForObject(FIND_CONFERENCE,
                new MapSqlParameterSource("callId", callId),
                rowMapper.findAllRosterMapper());
        return conference;
    }

    @Override
    public List<JoinedConferenceAttendee> getAttendees(String callId, long userId) {
        List<JoinedConferenceAttendee> attendees =
                jdbcTemplate.query(GET_CONFERENCE_USERS,
                        new MapSqlParameterSource()
                                .addValue("callId", callId)
                                .addValue("userId", userId), rowMapper.findAllAttendees());
        return attendees;
    }
}
