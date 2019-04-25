package cat.xlagunas.andrtc.roster;

public class JoinedRoster {
    public final long id;
    public final String username;
    public final String name;
    public final String email;
    public final String profilePic;
    public final String status;

    private JoinedRoster() {
        throw new UnsupportedOperationException();
    }

    private JoinedRoster(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.name = builder.name;
        this.email = builder.email;
        this.profilePic = builder.profilePic;
        this.status = builder.status;
    }


    public static class Builder {
        private long id;
        private String username;
        private String name;
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

        public Builder name(String name) {
            this.name = name;
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
