package cat.xlagunas.andrtc.repository;

import cat.xlagunas.andrtc.exception.ExistingRelationshipException;
import cat.xlagunas.andrtc.repository.model.JoinedRoster;
import cat.xlagunas.andrtc.repository.model.Roster;

import java.util.List;

public interface RosterRepository {

    long insertRoster(Roster roster) throws ExistingRelationshipException;

    boolean updateRelationship(long relationshipId, String status);

    Roster findRosterRelationship(long relationshipId);

    List<JoinedRoster> findAll(long userId);

    List<Long> findAllIds(long userId);

    List<JoinedRoster> findByStatus(long userId, String status);

    List<Long> findBothRelationships(long userId, long contactId);

    boolean removeRelationships(List<Long> ids);

    List<JoinedRoster> findByUsernameOrName(String query);
}
