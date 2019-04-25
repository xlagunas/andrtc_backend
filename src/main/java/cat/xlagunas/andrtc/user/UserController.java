package cat.xlagunas.andrtc.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cat.xlagunas.andrtc.common.AuthenticationUtils;

@RestController
@RequestMapping(value = "/user")
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
