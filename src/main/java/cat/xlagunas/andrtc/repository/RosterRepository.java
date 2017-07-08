package cat.xlagunas.andrtc.repository;

import cat.xlagunas.andrtc.exception.ExistingRelationshipException;
import cat.xlagunas.andrtc.model.FriendDto;
import cat.xlagunas.andrtc.model.FriendshipStatus;
import cat.xlagunas.andrtc.model.UserDto;

import java.util.List;

public interface RosterRepository {
    long insertRosterForUser(UserDto user, FriendDto friendDto) throws ExistingRelationshipException;

    boolean updateRelationship(UserDto user, FriendDto friendDto);

    List<FriendDto> findAll(long userId);

    List<FriendDto> findByStatus(long userId, FriendshipStatus status);
}
