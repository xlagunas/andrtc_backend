package cat.xlagunas.andrtc.model;

public class FriendDto {
    public final long id;
    public final String username;
    public final String name;
    public final String email;
    public final String profilePic;
    public final FriendshipStatus status;

    private FriendDto(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.name = builder.name;
        this.profilePic = builder.profilePic;
        this.email = builder.email;
        this.status = builder.status;
    }

    public static class Builder {
        private long id;
        private String username;
        private String name;
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

        public Builder status(FriendshipStatus status) {
            this.status = status;
            return this;
        }

        public FriendDto build() {
            return new FriendDto(this);
        }
    }

}
