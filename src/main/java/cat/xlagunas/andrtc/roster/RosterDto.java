package cat.xlagunas.andrtc.roster;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = RosterDto.Builder.class)
public class RosterDto {
    public final long id;
    public final long owner;
    public final long contact;
    public final String relationStatus;

    private RosterDto() {
        throw new UnsupportedOperationException();
    }

    private RosterDto(Builder builder) {
        this.id = builder.id;
        this.owner = builder.owner;
        this.contact = builder.contact;
        this.relationStatus = builder.relationStatus;
    }

    @JsonPOJOBuilder(buildMethodName = "build")
    public static class Builder {
        private long id;
        private long owner;
        private long contact;
        private String relationStatus;

        @JsonSetter(value = "id")
        public Builder id(long id) {
            this.id = id;
            return this;
        }

        @JsonSetter(value = "owner")
        public Builder owner(long owner) {
            this.owner = owner;
            return this;
        }

        @JsonSetter(value = "contact")
        public Builder contact(long contact) {
            this.contact = contact;
            return this;
        }

        @JsonSetter(value = "relationStatus")
        public Builder relationStatus(String relationStatus) {
            this.relationStatus = relationStatus;
            return this;
        }

        public RosterDto build() {
            return new RosterDto(this);
        }
    }
}
