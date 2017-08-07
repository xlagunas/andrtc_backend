package cat.xlagunas.andrtc.controller;

import cat.xlagunas.andrtc.exception.ExistingUserException;
import cat.xlagunas.andrtc.exception.UserNotFoundException;
import cat.xlagunas.andrtc.model.UserDto;
import cat.xlagunas.andrtc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/user", consumes = "application/json", produces = "application/json")
public class UserController {

    @Autowired
    private final UserService userService;

    @Value("${jwt.header}")
    private String tokenHeader;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    void createUser(@RequestBody UserDto userDto) throws ExistingUserException {
        userService.createUser(userDto);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    UserDto getUser(UsernamePasswordAuthenticationToken principal) throws UserNotFoundException {
        long principalId = AuthenticationUtils.getPrincipalId(principal);
        return userService.findUser(principalId);
    }

    @RequestMapping(value = "/search/{name}", method = RequestMethod.GET)
    List<UserDto> searchByUsername(UsernamePasswordAuthenticationToken principal, @PathVariable(name = "name") String username, HttpServletRequest request) {
        return userService.searchByUsername(username);
    }


    @RequestMapping(value = "/password", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void changePassword(UsernamePasswordAuthenticationToken principal, @RequestBody UserDto newUserData) {
        long principalId = AuthenticationUtils.getPrincipalId(principal);
        if (userService.updatePassword(principalId, newUserData.password)) {
            return;
        } else {
            throw new DataIntegrityViolationException("Request could not be performed");
        }
    }

    @RequestMapping(value = "/thumbnail", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void updateProfilePicture(UsernamePasswordAuthenticationToken principal, @RequestBody UserDto newUserData) {
        long principalId = AuthenticationUtils.getPrincipalId(principal);
        if (userService.updateProfilePicture(principalId, newUserData.profilePic)) {
            return;
        } else {
            throw new DataIntegrityViolationException("Request could not be performed");
        }
    }

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Data integrity violation")  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void conflict() {
        // Nothing to do
    }

}
