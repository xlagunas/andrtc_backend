package cat.xlagunas.andrtc.controller;

import cat.xlagunas.andrtc.exception.ExistingUserException;
import cat.xlagunas.andrtc.exception.UserNotFoundException;
import cat.xlagunas.andrtc.model.UserDto;
import cat.xlagunas.andrtc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
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

    @RequestMapping(value = "/search/{name}", method = RequestMethod.GET)
    List<UserDto> searchByUsername(Principal principal, @PathVariable(name = "name") String username, HttpServletRequest request) {
        return userService.searchByUsername(username);
    }


//    @RequestMapping(value="/password", method = RequestMethod.POST)
//    @ResponseStatus(HttpStatus.OK)
//    void changePassword(Principal principal, @RequestBody String password){
//        String name = principal.getName();
//        return userService.updatePassword()
//    }

    //boolean updatePassword(long userId, String password);

    //boolean updateProfilePicture(long userId, String profileUrl);

}
