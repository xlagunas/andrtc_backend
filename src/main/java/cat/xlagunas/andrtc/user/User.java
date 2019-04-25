package cat.xlagunas.andrtc.user;

import java.time.Instant;

public class User {
    public final String username;
    public final String firstname;
    public final String lastname;
    public final String email;
    public final String profilePic;
    public final String password;
    public final long id;
    public final Instant modifiedDate;

    private User() {
        throw new UnsupportedOperationException();
    }

    private User(Builder builder) {
        this.username = builder.username;
        this.firstname = builder.firstname;
        this.lastname = builder.lastname;
        this.email = builder.email;
        this.profilePic = builder.profilePic;
        this.password = builder.password;
        this.id = builder.id;
        this.modifiedDate = builder.modifiedDate;
    }


    public static class Builder {
        private long id;
        private String username;
        private String firstname;
        private String lastname;
        private String email;
        private String profilePic;
        private String password;
        private Instant modifiedDate;

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

        public Builder lastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder profilePic(String profilePic) {
            this.profilePic = profilePic;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder modifiedDate(Instant date) {
            this.modifiedDate = date;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
