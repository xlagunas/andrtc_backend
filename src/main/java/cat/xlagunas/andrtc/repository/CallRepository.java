package cat.xlagunas.andrtc.repository;

import cat.xlagunas.andrtc.repository.model.Conference;

public interface CallRepository {

    String createCall(long userId);

    void joinCall(long callId, long contactId);

    void rejectCall(long callId, long contactId);

    Conference getConferenceById(String callId);
}
