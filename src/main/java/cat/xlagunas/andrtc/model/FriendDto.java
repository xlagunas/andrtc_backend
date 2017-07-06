package cat.xlagunas.andrtc.model;

public class FriendDto {
    public final long id;
    public final String username;
    public final String firstname;
    public final String lastname;
    public final String email;
    public final String profilePic;
    public final FriendshipStatus status;

    private FriendDto(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.firstname = builder.firstname;
        this.lastname = builder.lastname;
        this.profilePic = builder.profilePic;
        this.email = builder.email;
        this.status = builder.status;
    }

    public static class Builder {
        private long id;
        private String username;
        private String firstname;
        private String lastname;
        private String profilePic;
        private String email;
        private FriendshipStatus status;

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

        public Builder profilePic(String profilePic) {
            this.profilePic = profilePic;
            return this;
        }

        public Builder status(FriendshipStatus status) {
            this.status = status;
            return this;
        }

        public FriendDto build() {
            return new FriendDto(this);
        }
    }

    public static enum FriendshipStatus {
        REJECTED, BLOCKED, PENDING, REQUESTED, ACCEPTED;
    }
}
