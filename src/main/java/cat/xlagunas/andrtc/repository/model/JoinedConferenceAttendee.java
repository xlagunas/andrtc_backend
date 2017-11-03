package cat.xlagunas.andrtc.repository.model;

public class JoinedConferenceAttendee {
    public final String conferenceId;
    public final boolean isStarter;
    public final JoinedRoster roster;

    public JoinedConferenceAttendee(String conferenceId, boolean isStarter, JoinedRoster roster) {
        this.conferenceId = conferenceId;
        this.isStarter = isStarter;
        this.roster = roster;
    }

    public static class Builder {
        private String conferenceId;
        private boolean isStarter;
        private JoinedRoster roster;

        public Builder setConferenceId(String conferenceId) {
            this.conferenceId = conferenceId;
            return this;
        }

        public Builder setIsStarter(boolean isStarter) {
            this.isStarter = isStarter;
            return this;
        }

        public Builder setRoster(JoinedRoster roster) {
            this.roster = roster;
            return this;
        }

        public JoinedConferenceAttendee build() {
            return new JoinedConferenceAttendee(conferenceId, isStarter, roster);
        }
    }
}
