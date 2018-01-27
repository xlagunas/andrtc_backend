package cat.xlagunas.andrtc.service;

import cat.xlagunas.andrtc.model.CallConverter;
import cat.xlagunas.andrtc.model.CallDetailsDto;
import cat.xlagunas.andrtc.model.CallDto;
import cat.xlagunas.andrtc.repository.CallRepository;
import cat.xlagunas.andrtc.repository.model.Conference;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class CallServiceImpl implements CallService {

    @Autowired
    CallRepository callRepository;

    public CallServiceImpl(CallRepository callRepository) {
        this.callRepository = callRepository;
    }

    @Override
    public CallDto createCall(long userId, List<Long> calleeList) {
        Conference call = callRepository.createCall();
        callRepository.updateCallContact(call.callId, userId, true, CallRepository.Status.ACCEPTED);

        for (long calleeId : calleeList) {
            callRepository.updateCallContact(call.callId, calleeId, false, CallRepository.Status.UNATTENDED);
        }

        return CallConverter.convertFrom(call);
    }

    @Override
    public void acceptCall(long userId, String callId) {
        callRepository.updateCallContact(callId, userId, false, CallRepository.Status.ACCEPTED);
    }

    @Override
    public void rejectCall(long userId, String callId) {
        callRepository.updateCallContact(callId, userId, false, CallRepository.Status.REJECTED);

    }

    @Override
    public List<CallDetailsDto> getCallDetails(long userId, String callId) {
        return callRepository.getAttendees(callId, userId)
                .stream()
                .map(CallConverter::convertFrom)
                .collect(Collectors.toList());
    }
}
