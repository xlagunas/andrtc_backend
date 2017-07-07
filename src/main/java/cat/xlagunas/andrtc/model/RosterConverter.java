package cat.xlagunas.andrtc.model;

import cat.xlagunas.andrtc.repository.JoinedRoster;
import cat.xlagunas.andrtc.repository.Roster;

public class RosterConverter {

    public static Roster convert(UserDto userDto, FriendDto friendDto) {
        return new Roster.Builder()
                .id(friendDto.id)
                .owner(userDto.id)
                .contact(friendDto.id)
                .relationStatus(friendDto.status.name())
                .build();
    }

    public static FriendDto convert(JoinedRoster user) {
        return new FriendDto.Builder()
                .id(user.id)
                .username(user.username)
                .firstname(user.firstname)
                .lastname(user.lastname)
                .profilePic(user.profilePic)
                .status(FriendshipStatus.valueOf(user.status))
                .build();
    }
}
