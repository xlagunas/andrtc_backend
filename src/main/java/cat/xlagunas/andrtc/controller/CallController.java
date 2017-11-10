package cat.xlagunas.andrtc.controller;

import cat.xlagunas.andrtc.model.CallDetailsDto;
import cat.xlagunas.andrtc.model.CallDto;
import cat.xlagunas.andrtc.model.CallMessageDto;
import cat.xlagunas.andrtc.model.CallParticipantsDto;
import cat.xlagunas.andrtc.service.CallService;
import cat.xlagunas.andrtc.service.PushNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/call")
public class CallController {

    @Autowired
    PushNotificationService pushNotificationService;

    @Autowired
    CallService callService;

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    CallDto createCall(UsernamePasswordAuthenticationToken principal, @RequestBody CallParticipantsDto callParticipantsDto) {
        long userId = AuthenticationUtils.getPrincipalId(principal);
        List<Long> contacts = callParticipantsDto.getParticipants().stream().map(roster -> roster.id).collect(Collectors.toList());
        CallDto callDto = callService.createCall(userId, contacts);
        pushNotificationService.sendPush(contacts, callDto);
        return callDto;
    }

    @RequestMapping(value = "/{callId}/join", method = RequestMethod.POST)
    void acceptCall(UsernamePasswordAuthenticationToken principal, @RequestParam String callId) {
        long userId = AuthenticationUtils.getPrincipalId(principal);

        callService.acceptCall(userId, callId);
        List<Long> callParticipantIdList = callService.getCallDetails(userId, callId)
                .stream()
                .map(contact -> contact.id)
                .collect(Collectors.toList());

        pushNotificationService.sendPush(callParticipantIdList,
                new CallMessageDto(userId, CallMessageDto.Status.JOINED));
    }

    @RequestMapping(value = "/{callId}/reject", method = RequestMethod.POST)
    void rejectCall(UsernamePasswordAuthenticationToken principal, @RequestParam String callId) {
        long userId = AuthenticationUtils.getPrincipalId(principal);
        callService.rejectCall(userId, callId);
        List<Long> callParticipantIdList = callService.getCallDetails(userId, callId)
                .stream()
                .map(contact -> contact.id)
                .collect(Collectors.toList());

        pushNotificationService.sendPush(callParticipantIdList,
                new CallMessageDto(userId, CallMessageDto.Status.REJECTED));

    }

    @RequestMapping(value = "/{callId}/attendees", method = RequestMethod.GET)
    List<CallDetailsDto> getCallAttendees(UsernamePasswordAuthenticationToken principal, @RequestParam String callId) {
        long userId = AuthenticationUtils.getPrincipalId(principal);
        return callService.getCallDetails(userId, callId);
    }
}
