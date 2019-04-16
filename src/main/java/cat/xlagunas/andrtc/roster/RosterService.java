package cat.xlagunas.andrtc.roster;

import java.util.List;

public interface RosterService {

    List<FriendDto> getAllFriends(long id);

    List<FriendDto> filterFriendsByStatus(long id, FriendshipStatus status);

    List<Long> getAllFriendshipIds(long userId);

    void acceptFriendship(long userId, long ownerId);

    void requestFriendship(long userId, long contactId) throws ExistingRelationshipException;

    void rejectFriendship(long userId, long contactId);

    void updateFriendshipStatus(long friendshipId, FriendshipStatus status);

    List<JoinedRoster> search(long userId, String query);
}
