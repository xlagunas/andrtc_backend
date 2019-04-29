package cat.xlagunas.andrtc.call;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;

import cat.xlagunas.andrtc.common.AuthenticationUtils;
import cat.xlagunas.andrtc.common.MessageType;
import cat.xlagunas.andrtc.push.PushMessageData;
import cat.xlagunas.andrtc.push.PushNotificationService;
import cat.xlagunas.andrtc.token.TokenService;

@RestController
@RequestMapping("/call")
public class CallController {

    private final TokenService tokenService;
    private final PushNotificationService pushNotificationService;
    private final CallService callService;

    public CallController(TokenService tokenService, PushNotificationService pushNotificationService, CallService callService) {
        this.tokenService = tokenService;
        this.pushNotificationService = pushNotificationService;
        this.callService = callService;
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public CallDto createCall(UsernamePasswordAuthenticationToken principal, @RequestBody CallParticipantsDto callParticipantsDto) {
        long userId = AuthenticationUtils.INSTANCE.getPrincipalId(principal);
        List<Long> contacts = callParticipantsDto.getParticipants().stream().map(roster -> roster.id).collect(Collectors.toList());
        CallDto callDto = callService.createCall(userId, contacts);

        PushMessageData messageData = new PushMessageData(MessageType.CREATE_CALL, ImmutableMap.<String, Object>builder().put("content", callDto).build());

        pushNotificationService.sendPush(tokenService.getUsersToken(contacts), messageData);
        return callDto;
    }

    @RequestMapping(value = "/{callId}/join", method = RequestMethod.POST)
    public void acceptCall(UsernamePasswordAuthenticationToken principal, @RequestParam String callId) {
        long userId = AuthenticationUtils.INSTANCE.getPrincipalId(principal);

        callService.acceptCall(userId, callId);
        List<Long> callParticipantIdList = callService.getCallDetails(userId, callId)
                .stream()
                .map(contact -> contact.id)
                .collect(Collectors.toList());

        PushMessageData messageData = new PushMessageData(MessageType.ACCEPT_CALL, ImmutableMap.<String, Object>builder().put("senderId", userId).build());

        List<String> pushTokens = tokenService.getUsersToken(callParticipantIdList);
        pushNotificationService.sendPush(pushTokens, messageData);
    }

    @RequestMapping(value = "/{callId}/reject", method = RequestMethod.POST)
    public void rejectCall(UsernamePasswordAuthenticationToken principal, @RequestParam String callId) {
        long userId = AuthenticationUtils.INSTANCE.getPrincipalId(principal);
        callService.rejectCall(userId, callId);
        List<Long> callParticipantIdList = callService.getCallDetails(userId, callId)
                .stream()
                .map(contact -> contact.id)
                .collect(Collectors.toList());

        PushMessageData messageData = new PushMessageData(MessageType.REJECT_CALL, ImmutableMap.<String, Object>builder().put("senderId", userId).build());

        pushNotificationService.sendPush(tokenService.getUsersToken(callParticipantIdList), messageData);

    }

    @RequestMapping(value = "/{callId}/attendees", method = RequestMethod.GET)
    public List<CallDetailsDto> getCallAttendees(UsernamePasswordAuthenticationToken principal, @RequestParam String callId) {
        long userId = AuthenticationUtils.INSTANCE.getPrincipalId(principal);
        return callService.getCallDetails(userId, callId);
    }

}
