package cat.xlagunas.andrtc.model;

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


    public static class Builder {
        private long id;
        private long owner;
        private long contact;
        private String relationStatus;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder owner(long owner) {
            this.owner = owner;
            return this;
        }

        public Builder contact(long contact) {
            this.contact = contact;
            return this;
        }

        public Builder relationStatus(String relationStatus) {
            this.relationStatus = relationStatus;
            return this;
        }

        public RosterDto build() {
            return new RosterDto(this);
        }
    }
}
