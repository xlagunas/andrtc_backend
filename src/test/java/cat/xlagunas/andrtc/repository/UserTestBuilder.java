package cat.xlagunas.andrtc.repository;

import cat.xlagunas.andrtc.model.UserDto;

public class UserTestBuilder {

    public static UserDto getUser() {
        return getUser(1, "aPass", "https://google.com/aPic2");
    }

    public static UserDto getUserWithId(int id) {
        return getUser(id, "aPass", "https://google.com/aPic2");
    }

    public static UserDto getUserWithPass(String pass) {
        return getUser(1, pass, "https://google.com/aPic2");
    }

    public static UserDto getUserWithProfilePic(String profilePic) {
        return getUser(1, "aPass", profilePic);
    }

    public static UserDto getUser(int id, String password, String profilePic) {
        return new UserDto.Builder()
                .id(id)
                .username("xlagunas"+System.currentTimeMillis())
                .firstname("Xavier")
                .lastname("Lagunas Calpe")
                .email(System.currentTimeMillis()+"xlagunas@gmail.com")
                .password(password)
                .profilePic(profilePic).build();

    }
}