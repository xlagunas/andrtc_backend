package cat.xlagunas.andrtc.repository.model;

public class ConferenceAttendee {
    public final long id;
    public final String conferenceId;
    public final long participantId;
    public final boolean isStarter;

    private ConferenceAttendee(long id, String conferenceId, long participantId, boolean isStarter) {
        this.id = id;
        this.conferenceId = conferenceId;
        this.participantId = participantId;
        this.isStarter = isStarter;
    }


    public static class Builder {
        private long id;
        private String conferenceId;
        private long participantId;
        private boolean isStarter;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setConferenceId(String conferenceId) {
            this.conferenceId = conferenceId;
            return this;
        }

        public Builder setParticipantId(long participantId) {
            this.participantId = participantId;
            return this;
        }

        public Builder setIsStarter(boolean isStarter) {
            this.isStarter = isStarter;
            return this;
        }

        public ConferenceAttendee build() {
            return new ConferenceAttendee(id, conferenceId, participantId, isStarter);
        }
    }
}
