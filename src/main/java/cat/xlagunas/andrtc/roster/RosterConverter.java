package cat.xlagunas.andrtc.roster;

import cat.xlagunas.andrtc.user.UserDto;

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
                .name(user.name)
                .email(user.email)
                .profilePic(user.profilePic)
                .status(FriendshipStatus.valueOf(user.status))
                .build();
    }
}
