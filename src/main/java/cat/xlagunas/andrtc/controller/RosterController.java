package cat.xlagunas.andrtc.controller;

import cat.xlagunas.andrtc.exception.ExistingRelationshipException;
import cat.xlagunas.andrtc.model.FriendDto;
import cat.xlagunas.andrtc.model.FriendshipStatus;
import cat.xlagunas.andrtc.model.RosterDto;
import cat.xlagunas.andrtc.model.UserDto;
import cat.xlagunas.andrtc.repository.model.JoinedRoster;
import cat.xlagunas.andrtc.repository.model.PushMessageData;
import cat.xlagunas.andrtc.service.PushNotificationService;
import cat.xlagunas.andrtc.service.RosterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/contact")
public class RosterController {

    @Autowired
    RosterService rosterService;

    @Autowired
    PushNotificationService pushService;

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    void createRelationship(UsernamePasswordAuthenticationToken principal, @RequestBody RosterDto rosterDto) throws ExistingRelationshipException {
        rosterService.requestFriendship(((UserDto) principal.getPrincipal()).id, rosterDto.contact);
        notifyFriendshipUpdate(rosterDto.contact, PushMessageData.MessageType.REQUEST_FRIENDSHIP);
    }

    @RequestMapping(value = "/search/{name}", method = RequestMethod.GET)
    List<JoinedRoster> searchByUsername(UsernamePasswordAuthenticationToken principal, @PathVariable(name = "name") String username, HttpServletRequest request) {
        return rosterService.search(AuthenticationUtils.getPrincipalId(principal), username);
    }

    @RequestMapping(value = "/{contactId}/accept", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void acceptRelationship(UsernamePasswordAuthenticationToken principal, @PathVariable("contactId") long contactId) {
        long userId = AuthenticationUtils.getPrincipalId(principal);
        rosterService.acceptFriendship(userId, contactId);
        notifyFriendshipUpdate(contactId, PushMessageData.MessageType.ACCEPT_FRIENDSHIP);
    }

    @RequestMapping(value = "/{contactId}/reject", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void rejectRelationship(UsernamePasswordAuthenticationToken principal, @PathVariable("contactId") long contactId) {
        long userId = AuthenticationUtils.getPrincipalId(principal);
        rosterService.rejectFriendship(userId, contactId);
        notifyFriendshipUpdate(contactId, PushMessageData.MessageType.REJECT_FRIENDSHIP);
    }

    @RequestMapping(value = "/{contactId}/update/{status}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void updateRelationship(UsernamePasswordAuthenticationToken principal, @PathVariable("contactId") long contactId,
                            @PathVariable("status") FriendshipStatus status) {
        rosterService.updateFriendshipStatus(AuthenticationUtils.getPrincipalId(principal), status);
    }

    @RequestMapping(value = {"/list", "/list/{filter}"}, method = RequestMethod.GET)
    List<FriendDto> getAllContacts(UsernamePasswordAuthenticationToken principal,
                                   @PathVariable("filter") Optional<FriendshipStatus> filter) {
        long principalId = AuthenticationUtils.getPrincipalId(principal);

        if (filter.isPresent()) {
            return rosterService.filterFriendsByStatus(principalId, filter.get());
        }

        return rosterService.getAllFriends(AuthenticationUtils.getPrincipalId(principal));
    }

    private void notifyFriendshipUpdate(long receiverId, PushMessageData.MessageType requestFriendship) {
        pushService.sendPush(receiverId,
                PushMessageData.builder(requestFriendship).build());
    }
}
