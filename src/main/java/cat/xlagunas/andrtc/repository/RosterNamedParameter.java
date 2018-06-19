package cat.xlagunas.andrtc.repository;

import cat.xlagunas.andrtc.repository.model.Roster;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RosterNamedParameter {

    public static MapSqlParameterSource insertRosterNamedParameter(Roster roster) {
        return new RosterNamedParameterBuilder()
                .withContact(roster.contact)
                .withOwner(roster.owner)
                .withRelationship(roster.relationStatus)
                .build();
    }

    public static MapSqlParameterSource updateRosterNamedParameter(long relationshipId, String status) {
        return new RosterNamedParameterBuilder()
                .withId(relationshipId)
                .withRelationship(status)
                .build();
    }

    public static MapSqlParameterSource findByStatus(long userId, String status) {
        return new RosterNamedParameterBuilder()
                .withRelationship(status)
                .withOwner(userId)
                .build();
    }

    public static MapSqlParameterSource findPair(long userId, long contactId) {
        return new RosterNamedParameterBuilder()
                .withOwner(userId)
                .withContact(contactId)
                .build();
    }

    public static MapSqlParameterSource findRoster(long relationshipId) {
        return new RosterNamedParameterBuilder()
                .withId(relationshipId)
                .build();
    }


    public static MapSqlParameterSource findAll(long userId) {
        return new RosterNamedParameterBuilder()
                .withOwner(userId)
                .build();
    }

    public static MapSqlParameterSource deleteById(List<Long> ids) {
        return new MapSqlParameterSource("idList", ids);
    }

    public static MapSqlParameterSource findAllIdOnly(long userId) {
        return new RosterNamedParameterBuilder().withOwner(userId).build();
    }

    private static class RosterNamedParameterBuilder {
        private Map<String, Object> map;

        RosterNamedParameterBuilder() {
            map = new HashMap<>(4);
        }

        public RosterNamedParameterBuilder withContact(long contact) {
            map.put("contact", contact);
            return this;
        }

        public RosterNamedParameterBuilder withOwner(long owner) {
            map.put("owner", owner);
            return this;
        }

        public RosterNamedParameterBuilder withRelationship(String relationship) {
            map.put("status", relationship);
            return this;
        }

        public RosterNamedParameterBuilder withId(long id) {
            map.put("id", id);
            return this;
        }

        public MapSqlParameterSource build() {
            return new MapSqlParameterSource(map);
        }
    }


}
