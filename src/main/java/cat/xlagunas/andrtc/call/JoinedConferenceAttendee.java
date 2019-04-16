package cat.xlagunas.andrtc.call;

import cat.xlagunas.andrtc.roster.JoinedRoster;

public class JoinedConferenceAttendee {
    public final boolean isStarter;
    public final JoinedRoster roster;

    public JoinedConferenceAttendee(boolean isStarter, JoinedRoster roster) {
        this.isStarter = isStarter;
        this.roster = roster;
    }

    public static class Builder {
        private boolean isStarter;
        private JoinedRoster roster;

        public Builder setIsStarter(boolean isStarter) {
            this.isStarter = isStarter;
            return this;
        }

        public Builder setRoster(JoinedRoster roster) {
            this.roster = roster;
            return this;
        }

        public JoinedConferenceAttendee build() {
            return new JoinedConferenceAttendee(isStarter, roster);
        }
    }
}
