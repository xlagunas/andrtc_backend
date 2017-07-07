package cat.xlagunas.andrtc.repository;


import cat.xlagunas.andrtc.exception.ExistingRelationshipException;
import cat.xlagunas.andrtc.model.FriendDto;
import cat.xlagunas.andrtc.model.FriendshipStatus;
import cat.xlagunas.andrtc.model.UserDto;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RosterRepositoryImpl implements RosterRepository {

    private final static String UPDATE_RELATIONSHIP = "UPDATE ROSTER SET STATUS = ? WHERE OWNER = ? AND CONTACT = ?";
    private final static String FIND_FRIENDSHIPS = "SELECT USER.ID, EMAIL, USERNAME, FIRST_NAME, LAST_NAME, PROFILE_PIC, STATUS" +
            " FROM ROSTER LEFT JOIN USER ON ROSTER.CONTACT = USER.ID WHERE OWNER = ?";
    private final static String FIND_FILTERED_FRIENDSHIPS = "SELECT USER.ID, EMAIL, USERNAME, FIRST_NAME, LAST_NAME, PROFILE_PIC, STATUS" +
            " FROM ROSTER LEFT JOIN USER ON ROSTER.CONTACT = USER.ID WHERE OWNER = ? AND STATUS = ?";
    private final JdbcTemplate jdbcTemplate;
    private final FriendRowMapper rowMapper;

    public RosterRepositoryImpl(JdbcTemplate template, FriendRowMapper rowMapper) {
        this.jdbcTemplate = template;
        this.rowMapper = rowMapper;
    }

    @Override
    public long insertRosterForUser(UserDto user, FriendDto friendDto) throws ExistingRelationshipException {
        try {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            simpleJdbcInsert.withTableName("ROSTER")
                    .usingGeneratedKeyColumns("ID");
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("OWNER", user.id);
            parameters.put("CONTACT", friendDto.id);
            parameters.put("STATUS", friendDto.status.name());

            return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        } catch (DuplicateKeyException ex) {
            throw new ExistingRelationshipException(ex);
        }
    }

    @Override
    public boolean updateRelationship(UserDto user, FriendDto friendDto) {
        long affectedRows = jdbcTemplate.update(UPDATE_RELATIONSHIP, friendDto.status.name(), user.id, friendDto.id);
        return affectedRows > 0;
    }

    @Override
    public List<FriendDto> findAll(long userId) {
        return jdbcTemplate.query(FIND_FRIENDSHIPS, new Object[]{userId}, rowMapper.findAllRosterMapper());
    }

    @Override
    public List<FriendDto> findByStatus(long userId, FriendshipStatus status){
        return jdbcTemplate.query(FIND_FILTERED_FRIENDSHIPS, new Object[]{userId, status.name()}, rowMapper.findAllRosterMapper());

    }
}
