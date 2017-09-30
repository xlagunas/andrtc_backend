package cat.xlagunas.andrtc.controller;

import cat.xlagunas.andrtc.exception.ExistingRelationshipException;
import cat.xlagunas.andrtc.model.RosterDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/call")
public class CallController {

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    void createCall(UsernamePasswordAuthenticationToken principal, @RequestBody RosterDto rosterDto) throws ExistingRelationshipException {
    }

    @RequestMapping(value = "/{callId}/accept", method = RequestMethod.POST)
    void acceptCall(UsernamePasswordAuthenticationToken principal, @RequestParam long callId) {

    }

    @RequestMapping(value = "/{callId}/reject", method = RequestMethod.POST)
    void rejectCall(UsernamePasswordAuthenticationToken principal, @RequestParam long callId) {

    }
}
