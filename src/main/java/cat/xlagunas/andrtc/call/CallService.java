package cat.xlagunas.andrtc.call;

import java.util.List;

public interface CallService {

    CallDto createCall(long userId, List<Long> calleeList);

    void acceptCall(long userId, String callId);

    void rejectCall(long userId, String callId);

    /*Returns a list of users that are already in the conference and have accepted it
    (note self user should not be in the list)*/
    List<CallDetailsDto> getCallDetails(long userId, String callId);
}
