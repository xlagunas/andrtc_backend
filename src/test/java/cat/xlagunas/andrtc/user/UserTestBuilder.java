package cat.xlagunas.andrtc.user;

import java.time.Instant;

public class UserTestBuilder {

    public static User getUser() {
        return getUser(1, "aPass", "https://google.com/aPic2");
    }

    public static User getUserWithId(int id) {
        return getUser(id, "aPass", "https://google.com/aPic2");
    }

    public static User getUserWithPass(String pass) {
        return getUser(1, pass, "https://google.com/aPic2");
    }

    public static User getUserWithProfilePic(String profilePic) {
        return getUser(1, "aPass", profilePic);
    }

    public static User getUser(int id, String password, String profilePic) {
        return new User.Builder()
                .id(id)
                .username("xlagunas" + Instant.now() + password)
                .firstname("Xavier")
                .lastname("Lagunas Calpe")
                .email(System.currentTimeMillis() + "xlagunas@gmail.com" + password)
                .password(password)
                .profilePic(profilePic).build();

    }
}