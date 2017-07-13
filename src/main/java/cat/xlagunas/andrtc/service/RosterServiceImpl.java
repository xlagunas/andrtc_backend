package cat.xlagunas.andrtc.service;

import cat.xlagunas.andrtc.model.FriendDto;
import cat.xlagunas.andrtc.model.FriendshipStatus;

import java.util.List;

public class RosterServiceImpl implements RosterService {

    @Override
    public List<FriendDto> getAllFriends(long id) {
        return null;
    }

    @Override
    public List<FriendDto> filterFriendsByStatus(long id, FriendshipStatus status) {
        return null;
    }

    @Override
    public void acceptFriendship(long userId, long ownerId) {

    }

    @Override
    public void requestFriendship(long userId, long contactId) {

    }

    @Override
    public void rejectFriendship(long userId, long contactId) {

    }

    @Override
    public void updateFriendshipStatus(long friendshipId, FriendshipStatus status) {

    }
}
