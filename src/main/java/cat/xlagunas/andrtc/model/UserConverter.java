package cat.xlagunas.andrtc.model;

import cat.xlagunas.andrtc.repository.User;

public class UserConverter {

    public static UserDto convert(User user){
        return new UserDto.Builder()
                .id(user.id)
                .username(user.username)
                .firstname(user.firstname)
                .lastname(user.lastname)
                .password(user.password)
                .profilePic(user.profilePic)
                .build();
    }

    public static User convert(UserDto user) {
        return new User.Builder()
                .id(user.id)
                .username(user.username)
                .firstname(user.firstname)
                .lastname(user.lastname)
                .password(user.password)
                .profilePic(user.profilePic)
                .build();
    }
}
