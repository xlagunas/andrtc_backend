package cat.xlagunas.andrtc.call;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cat.xlagunas.andrtc.common.AuthenticationUtils;
import cat.xlagunas.andrtc.common.MessageType;
import cat.xlagunas.andrtc.push.PushMessageData;
import cat.xlagunas.andrtc.push.PushNotificationService;
import cat.xlagunas.andrtc.token.TokenService;

@RestController
@RequestMapping("/call")
public class CallController {

    @Autowired
    TokenService tokenService;

    @Autowired
    PushNotificationService pushNotificationService;

    @Autowired
    CallService callService;

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    CallDto createCall(UsernamePasswordAuthenticationToken principal, @RequestBody CallParticipantsDto callParticipantsDto) {
        long userId = AuthenticationUtils.getPrincipalId(principal);
        List<Long> contacts = callParticipantsDto.getParticipants().stream().map(roster -> roster.id).collect(Collectors.toList());
        CallDto callDto = callService.createCall(userId, contacts);

        PushMessageData messageData = PushMessageData.builder(MessageType.CREATE_CALL)
                .addParams("content", callDto)
                .build();
        pushNotificationService.sendPush(tokenService.getUsersToken(contacts), messageData);
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

        PushMessageData messageData = PushMessageData.builder(MessageType.ACCEPT_CALL)
                .addParams("senderId", userId)
                .build();

        List<String> pushTokens = tokenService.getUsersToken(callParticipantIdList);
        pushNotificationService.sendPush(pushTokens, messageData);
    }

    @RequestMapping(value = "/{callId}/reject", method = RequestMethod.POST)
    void rejectCall(UsernamePasswordAuthenticationToken principal, @RequestParam String callId) {
        long userId = AuthenticationUtils.getPrincipalId(principal);
        callService.rejectCall(userId, callId);
        List<Long> callParticipantIdList = callService.getCallDetails(userId, callId)
                .stream()
                .map(contact -> contact.id)
                .collect(Collectors.toList());

        PushMessageData messageData = PushMessageData.builder(MessageType.REJECT_CALL)
                .addParams("senderId", userId)
                .build();
        pushNotificationService.sendPush(tokenService.getUsersToken(callParticipantIdList), messageData);

    }

    @RequestMapping(value = "/{callId}/attendees", method = RequestMethod.GET)
    List<CallDetailsDto> getCallAttendees(UsernamePasswordAuthenticationToken principal, @RequestParam String callId) {
        long userId = AuthenticationUtils.getPrincipalId(principal);
        return callService.getCallDetails(userId, callId);
    }

}
