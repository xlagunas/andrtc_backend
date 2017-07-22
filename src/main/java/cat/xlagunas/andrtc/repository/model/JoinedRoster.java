package cat.xlagunas.andrtc.repository.model;

public class JoinedRoster {
    public final long id;
    public final String username;
    public final String firstname;
    public final String lastname;
    public final String email;
    public final String profilePic;
    public final String status;

    private JoinedRoster() {
        throw new UnsupportedOperationException();
    }

    private JoinedRoster(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.firstname = builder.firstname;
        this.lastname = builder.lastname;
        this.email = builder.email;
        this.profilePic = builder.profilePic;
        this.status = builder.status;
    }


    public static class Builder {
        private long id;
        private String username;
        private String firstname;
        private String lastname;
        private String email;
        private String profilePic;
        private String status;

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

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public JoinedRoster build() {
            return new JoinedRoster(this);
        }
    }
}
