package cat.xlagunas.andrtc.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = CallDetailsDto.Builder.class)
public class CallDetailsDto {

    public final long id;
    public final String username;
    public final String name;
    public final String email;
    public final String profilePic;
    public final String status;
    public final boolean isStarter;

    private CallDetailsDto() {
        throw new UnsupportedOperationException("Black magic not allowed here");
    }

    private CallDetailsDto(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.name = builder.name;
        this.email = builder.email;
        this.profilePic = builder.profilePic;
        this.status = builder.status;
        this.isStarter = builder.isStarter;
    }

    @JsonPOJOBuilder(buildMethodName = "build")
    public static class Builder {
        private long id;
        private String username;
        private String name;
        private String email;
        private String profilePic;
        private String status;
        private boolean isStarter;

        @JsonSetter(value = "id")
        public Builder id(long id) {
            this.id = id;
            return this;
        }

        @JsonSetter(value = "username")
        public Builder username(String username) {
            this.username = username;
            return this;
        }

        @JsonSetter(value = "name")
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        @JsonSetter(value = "email")
        public Builder email(String email) {
            this.email = email;
            return this;
        }

        @JsonSetter(value = "profilePic")
        public Builder profilePic(String profilePic) {
            this.profilePic = profilePic;
            return this;
        }

        @JsonSetter(value = "status")
        public Builder status(String status) {
            this.status = status;
            return this;
        }

        @JsonSetter(value = "isStarter")
        public Builder isStarter(boolean isStarter) {
            this.isStarter = isStarter;
            return this;
        }

        public CallDetailsDto build() {
            return new CallDetailsDto(this);
        }
    }
}
