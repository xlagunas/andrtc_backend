package cat.xlagunas.andrtc.repository;


import cat.xlagunas.andrtc.exception.ExistingRelationshipException;
import cat.xlagunas.andrtc.repository.model.JoinedRoster;
import cat.xlagunas.andrtc.repository.model.Roster;
import cat.xlagunas.andrtc.repository.rowmapper.RosterRowMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;

public class RosterRepositoryImpl implements RosterRepository {

    private final static String INSERT_RELATIONSHIP =
            "INSERT INTO ROSTER (OWNER, CONTACT, STATUS) VALUES (:owner, :contact, :status)";

    private final static String UPDATE_RELATIONSHIP =
            "UPDATE ROSTER SET STATUS = :status WHERE ROSTER.ID = :id";

    private final static String FIND_FRIENDSHIPS =
            "SELECT USER.ID, EMAIL, USERNAME, FIRST_NAME, LAST_NAME, PROFILE_PIC, STATUS FROM ROSTER " +
                    "LEFT JOIN USER ON ROSTER.CONTACT = USER.ID WHERE OWNER = :owner";

    private final static String FIND_FILTERED_FRIENDSHIPS =
            "SELECT USER.ID, EMAIL, USERNAME, FIRST_NAME, LAST_NAME, PROFILE_PIC, STATUS FROM ROSTER " +
                    "LEFT JOIN USER ON ROSTER.CONTACT = USER.ID WHERE OWNER = :owner AND STATUS = :status";

    private final static String FIND_FRIENDSHIP =
            "SELECT * FROM ROSTER WHERE ROSTER.ID = :id";

    private final static String FIND_PAIR_FRIENDSHIP_IDS =
            "SELECT ID FROM ROSTER WHERE (ROSTER.CONTACT = :contact AND ROSTER.OWNER = :owner) OR (ROSTER.CONTACT = :owner AND ROSTER.OWNER = :contact)";

    private final static String REMOVE_FRIENDSHIP_BY_ID =
            "DELETE FROM ROSTER WHERE ROSTER.ID IN (:idList)";

    private final static String FIND_FRIENDSHIP_ID_ONLY =
            "SELECT ID FROM ROSTER WHERE ROSTER.OWNER = :owner";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RosterRowMapper rowMapper;

    public RosterRepositoryImpl(NamedParameterJdbcTemplate template, RosterRowMapper rowMapper) {
        this.jdbcTemplate = template;
        this.rowMapper = rowMapper;
    }

    @Override
    public long insertRoster(Roster roster) throws ExistingRelationshipException {
        try {
            KeyHolder holder = new GeneratedKeyHolder();
            long id = jdbcTemplate.update(INSERT_RELATIONSHIP,
                    RosterNamedParameter.insertRosterNamedParameter(roster),
                    holder);
            return holder.getKey().longValue();
        } catch (DuplicateKeyException ex) {
            throw new ExistingRelationshipException(ex);
        }
    }

    @Override
    public boolean updateRelationship(long relationshipId, String status) {
        long affectedRows = jdbcTemplate.update(UPDATE_RELATIONSHIP, RosterNamedParameter.updateRosterNamedParameter(relationshipId, status));
        return affectedRows > 0;
    }

    @Override
    public Roster findRosterRelationship(long relationshipId) {
        return jdbcTemplate.queryForObject(FIND_FRIENDSHIP, RosterNamedParameter.findRoster(relationshipId), rowMapper.getRoster());
    }

    @Override
    public List<JoinedRoster> findAll(long userId) {
        return jdbcTemplate.query(FIND_FRIENDSHIPS, RosterNamedParameter.findAll(userId), rowMapper.findAllRosterMapper());
    }

    @Override
    public List<Long> findAllIds(long userId) {
        return jdbcTemplate.query(FIND_FRIENDSHIP_ID_ONLY, RosterNamedParameter.findAllIdOnly(userId), rowMapper.getRelationshipIds());
    }

    @Override
    public List<JoinedRoster> findByStatus(long userId, String status) {
        return jdbcTemplate.query(FIND_FILTERED_FRIENDSHIPS, RosterNamedParameter.findByStatus(userId, status), rowMapper.findAllRosterMapper());

    }

    public List<Long> findBothRelationships(long userId, long contactId) {
        return jdbcTemplate.query(FIND_PAIR_FRIENDSHIP_IDS, RosterNamedParameter.findPair(userId, contactId), rowMapper.getRelationshipIds());
    }

    @Override
    public boolean removeRelationships(List<Long> ids) {
        int updatedRows = jdbcTemplate.update(REMOVE_FRIENDSHIP_BY_ID, RosterNamedParameter.deleteById(ids));
        return updatedRows == 2;
    }
}
