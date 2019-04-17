package cat.xlagunas.andrtc.call;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = CallDetailsDto.Builder.class)
class CallDetailsDto {

    final long id;
    final String username;
    final String name;
    final String email;
    final String profilePic;
    final String status;
    final boolean isStarter;

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
    static class Builder {
        private long id;
        private String username;
        private String name;
        private String email;
        private String profilePic;
        private String status;
        private boolean isStarter;

        @JsonSetter(value = "id")
        Builder id(long id) {
            this.id = id;
            return this;
        }

        @JsonSetter(value = "username")
        Builder username(String username) {
            this.username = username;
            return this;
        }

        @JsonSetter(value = "name")
        Builder name(String name) {
            this.name = name;
            return this;
        }

        @JsonSetter(value = "email")
        Builder email(String email) {
            this.email = email;
            return this;
        }

        @JsonSetter(value = "profilePic")
        Builder profilePic(String profilePic) {
            this.profilePic = profilePic;
            return this;
        }

        @JsonSetter(value = "status")
        Builder status(String status) {
            this.status = status;
            return this;
        }

        @JsonSetter(value = "isStarter")
        Builder isStarter(boolean isStarter) {
            this.isStarter = isStarter;
            return this;
        }

        CallDetailsDto build() {
            return new CallDetailsDto(this);
        }
    }
}
