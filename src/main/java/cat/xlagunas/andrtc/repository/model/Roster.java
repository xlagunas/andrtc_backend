package cat.xlagunas.andrtc.repository.model;

public class Roster {
    public final long id;
    public final long owner;
    public final long contact;
    public final String relationStatus;

    private Roster() {
        throw new UnsupportedOperationException();
    }

    private Roster(Builder builder) {
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

        public Builder(Roster roster) {
            this.id = roster.id;
            this.contact = roster.contact;
            this.owner = roster.owner;
            this.relationStatus = roster.relationStatus;
        }

        public Builder() {
            super();
        }

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

        public Roster build() {
            return new Roster(this);
        }
    }


}
