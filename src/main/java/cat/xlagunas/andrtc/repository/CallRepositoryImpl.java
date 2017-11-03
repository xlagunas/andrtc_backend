package cat.xlagunas.andrtc.repository;

import cat.xlagunas.andrtc.repository.model.Conference;
import cat.xlagunas.andrtc.repository.rowmapper.ConferenceRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.Date;
import java.time.Instant;
import java.util.UUID;

public class CallRepositoryImpl implements CallRepository {
    private final static String CREATE_CONFERENCE = "INSERT INTO CONFERENCE (ID, DATE) VALUES (:callId, :date)";
    private final static String FIND_CONFERENCE = "SELECT * FROM CONFERENCE WHERE ID = :callId";
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ConferenceRowMapper rowMapper;

    public CallRepositoryImpl(NamedParameterJdbcTemplate template, ConferenceRowMapper rowMapper) {
        this.jdbcTemplate = template;
        this.rowMapper = rowMapper;
    }

    @Override
    public String createCall(long userId) {
        Conference conference = new Conference(UUID.randomUUID().toString(),
                Date.from(Instant.now()));
        jdbcTemplate.update(CREATE_CONFERENCE,
                new MapSqlParameterSource("callId", conference.callId)
                        .addValue("date", conference.date));

        return conference.callId;
    }

    @Override
    public void joinCall(long callId, long contactId) {

    }

    @Override
    public void rejectCall(long callId, long contactId) {

    }

    @Override
    public Conference getConferenceById(String callId) {
        Conference conference = jdbcTemplate.queryForObject(FIND_CONFERENCE,
                new MapSqlParameterSource("callId", callId),
                rowMapper.findAllRosterMapper());
        return conference;
    }
}
