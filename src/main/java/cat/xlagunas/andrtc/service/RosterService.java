package cat.xlagunas.andrtc.service;

import cat.xlagunas.andrtc.model.FriendDto;
import cat.xlagunas.andrtc.model.FriendshipStatus;

import java.util.List;

public interface RosterService {

    List<FriendDto> getAllFriends(long id);

    List<FriendDto> filterFriendsByStatus(long id, FriendshipStatus status);

    void acceptFriendship(long userId, long ownerId);

    void requestFriendship(long userId, long contactId);

    void rejectFriendship(long userId, long contactId);

    void updateFriendshipStatus(long friendshipId, FriendshipStatus status);

}
