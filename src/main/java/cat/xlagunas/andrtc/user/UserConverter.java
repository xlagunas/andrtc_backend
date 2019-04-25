package cat.xlagunas.andrtc.user;

public class UserConverter {

    public static UserDto convert(User user) {
        return new UserDto.Builder()
                .id(user.id)
                .username(user.username)
                .firstname(user.firstname)
                .lastname(user.lastname)
                .password(user.password)
                .profilePic(user.profilePic)
                .email(user.email)
                .passwordUpdate(user.modifiedDate)
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
                .email(user.email)
                .modifiedDate(user.passwordUpdate)
                .build();
    }
}
