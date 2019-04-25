package cat.xlagunas.andrtc.roster;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cat.xlagunas.andrtc.common.AuthenticationUtils;
import cat.xlagunas.andrtc.common.MessageType;
import cat.xlagunas.andrtc.push.PushMessageData;
import cat.xlagunas.andrtc.push.PushNotificationService;
import cat.xlagunas.andrtc.token.TokenService;

@RestController
@RequestMapping(value = "/contact")
public class RosterController {

    final RosterService rosterService;
    final TokenService tokenService;
    final PushNotificationService pushNotificationService;

    RosterController(RosterService rosterService, TokenService tokenService, PushNotificationService pushNotificationService) {
        this.rosterService = rosterService;
        this.tokenService = tokenService;
        this.pushNotificationService = pushNotificationService;
    }

    @RequestMapping(value = "/{contactId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public void createRelationship(UsernamePasswordAuthenticationToken principal, @PathVariable(name = "contactId") long contactId) throws ExistingRelationshipException {
        long userId = AuthenticationUtils.getPrincipalId(principal);
        rosterService.requestFriendship(userId, contactId);
        notifyFriendshipUpdate(contactId, MessageType.REQUEST_FRIENDSHIP);
    }

    @RequestMapping(value = "/search/{name}", method = RequestMethod.GET)
    public List<JoinedRoster> searchByUsername(UsernamePasswordAuthenticationToken principal, @PathVariable(name = "name") String username,
                                               HttpServletRequest request) {
        return rosterService.search(AuthenticationUtils.getPrincipalId(principal), username);
    }

    @RequestMapping(value = "/{contactId}/accept", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void acceptRelationship(UsernamePasswordAuthenticationToken principal, @PathVariable("contactId") long contactId) {
        long userId = AuthenticationUtils.getPrincipalId(principal);
        rosterService.acceptFriendship(userId, contactId);
        notifyFriendshipUpdate(contactId, MessageType.ACCEPT_FRIENDSHIP);
    }

    @RequestMapping(value = "/{contactId}/reject", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void rejectRelationship(UsernamePasswordAuthenticationToken principal, @PathVariable("contactId") long contactId) {
        long userId = AuthenticationUtils.getPrincipalId(principal);
        rosterService.rejectFriendship(userId, contactId);
        notifyFriendshipUpdate(contactId, MessageType.REJECT_FRIENDSHIP);
    }

    @RequestMapping(value = "/{contactId}/update/{status}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateRelationship(UsernamePasswordAuthenticationToken principal, @PathVariable("contactId") long contactId,
                                   @PathVariable("status") FriendshipStatus status) {
        rosterService.updateFriendshipStatus(AuthenticationUtils.getPrincipalId(principal), status);
    }

    @RequestMapping(value = {"/list", "/list/{filter}"}, method = RequestMethod.GET)
    public List<FriendDto> getAllContacts(UsernamePasswordAuthenticationToken principal,
                                          @PathVariable("filter") Optional<FriendshipStatus> filter) {
        long principalId = AuthenticationUtils.getPrincipalId(principal);

        if (filter.isPresent()) {
            return rosterService.filterFriendsByStatus(principalId, filter.get());
        }

        return rosterService.getAllFriends(AuthenticationUtils.getPrincipalId(principal));
    }

    private void notifyFriendshipUpdate(long receiverId, MessageType requestFriendship) {
        pushNotificationService.sendPush(tokenService.getUsersToken(receiverId),
                new PushMessageData(requestFriendship, Collections.emptyMap()));
    }
}
