package cat.xlagunas.andrtc.repository;

import cat.xlagunas.andrtc.repository.model.Conference;
import cat.xlagunas.andrtc.repository.model.JoinedConferenceAttendee;

import java.util.List;

public interface CallRepository {
    enum Status {ACCEPTED, REJECTED, UNATTENDED}

    Conference createCall();

    void updateCallContact(String callId, long userId, boolean isStarter, Status status);

    Conference getConferenceById(String callId);

    List<JoinedConferenceAttendee> getAttendees(String callId, long userId);
}
