package cat.xlagunas.andrtc.call;

import java.util.List;

public interface CallRepository {
    Conference createCall();

    void updateCallContact(String callId, long userId, boolean isStarter, Status status);

    Conference getConferenceById(String callId);

    List<JoinedConferenceAttendee> getAttendees(String callId, long userId);

    enum Status {ACCEPTED, REJECTED, UNATTENDED}
}
