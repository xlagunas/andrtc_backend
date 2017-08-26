package cat.xlagunas.andrtc.controller;

import cat.xlagunas.andrtc.exception.ExistingRelationshipException;
import cat.xlagunas.andrtc.model.FriendDto;
import cat.xlagunas.andrtc.model.FriendshipStatus;
import cat.xlagunas.andrtc.model.RosterDto;
import cat.xlagunas.andrtc.model.UserDto;
import cat.xlagunas.andrtc.repository.model.JoinedRoster;
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

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    void createRelationship(UsernamePasswordAuthenticationToken principal, @RequestBody RosterDto rosterDto) throws ExistingRelationshipException {
        rosterService.requestFriendship(((UserDto) principal.getPrincipal()).id, rosterDto.contact);
    }

    @RequestMapping(value = "/search/{name}", method = RequestMethod.GET)
    List<JoinedRoster> searchByUsername(UsernamePasswordAuthenticationToken principal, @PathVariable(name = "name") String username, HttpServletRequest request) {
        return rosterService.search(username);
    }

    @RequestMapping(value = "/{contactId}/accept", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void acceptRelationship(UsernamePasswordAuthenticationToken principal, @PathVariable("contactId") long contactId) {
        rosterService.acceptFriendship(AuthenticationUtils.getPrincipalId(principal), contactId);
    }

    @RequestMapping(value = "/{contactId}/reject", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void rejectRelationship(UsernamePasswordAuthenticationToken principal, @PathVariable("contactId") long contactId) {
        rosterService.rejectFriendship(AuthenticationUtils.getPrincipalId(principal), contactId);
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

}
