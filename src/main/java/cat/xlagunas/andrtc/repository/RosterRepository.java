package cat.xlagunas.andrtc.repository;

import cat.xlagunas.andrtc.exception.ExistingRelationshipException;
import cat.xlagunas.andrtc.model.FriendDto;
import cat.xlagunas.andrtc.model.FriendshipStatus;
import cat.xlagunas.andrtc.model.UserDto;

import java.util.List;

public interface RosterRepository {


    long insertRosterForUser(Roster roster) throws ExistingRelationshipException;

    boolean updateRelationship(Roster roster);

    Roster findRosterRelationship(long idRelationship);

    List<JoinedRoster> findAll(long userId);

    List<JoinedRoster> findByStatus(long userId, FriendshipStatus status);
}
