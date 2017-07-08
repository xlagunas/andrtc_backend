package cat.xlagunas.andrtc.service;

import cat.xlagunas.andrtc.model.FriendDto;
import cat.xlagunas.andrtc.model.FriendshipStatus;

import java.util.List;

public interface RosterService {

    List<FriendDto> getAllFriends(long id);

    List<FriendDto> filterFriendsByStatus(long id, FriendshipStatus status);

    /**
     * Since each user has its own roster relationship row with the other user, if the roster user accepts the relationship
     * 2 row updates on database, one for each user, need to be performed
     *
     * @param friendshipId
     */
    void acceptFriendship(long friendshipId);

    void requestFriendship(long ownerId, long contactId);

    void updateFriendshipStatus(long friendshipId, FriendshipStatus status);

}
