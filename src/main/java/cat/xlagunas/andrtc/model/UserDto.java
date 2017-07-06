package cat.xlagunas.andrtc.model;

public class UserDto {
    public final long id;
    public final String username;
    public final String firstname;
    public final String lastname;
    public final String password;
    public final String email;
    public final String profilePic;

    private UserDto(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.firstname = builder.firstname;
        this.lastname = builder.lastname;
        this.password = builder.password;
        this.profilePic = builder.profilePic;
        this.email = builder.email;
    }

    public static class Builder {
        private long id;
        private String username;
        private String firstname;
        private String lastname;
        private String password;
        private String profilePic;
        private String email;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }
        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder lastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder profilePic(String profilePic) {
            this.profilePic = profilePic;
            return this;
        }

        public UserDto build() {
            return new UserDto(this);
        }
    }
}
