package cat.xlagunas.andrtc.token;

public class Token {

    public final long id;
    public final long userId;
    public final String value;
    public final String platform;

    public Token(Builder builder) {
        this.id = builder.id;
        this.userId = builder.userId;
        this.value = builder.value;
        this.platform = builder.platform;
    }


    public static class Builder {
        private long id;
        private long userId;
        private String value;
        private String platform;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder userId(long userId) {
            this.userId = userId;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public Builder platform(String platform) {
            this.platform = platform;
            return this;
        }

        public Token build() {
            return new Token(this);
        }
    }
}
