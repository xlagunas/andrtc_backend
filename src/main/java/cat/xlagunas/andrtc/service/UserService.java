package cat.xlagunas.andrtc.service;

import cat.xlagunas.andrtc.exception.ExistingUserException;
import cat.xlagunas.andrtc.exception.UserNotFoundException;
import cat.xlagunas.andrtc.model.UserDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserDto findUser(long idUser) throws UserNotFoundException;

    UserDto findUser(String username) throws UserNotFoundException;

    List<UserDto> searchByUsername(String username);

    void createUser(UserDto userDto) throws ExistingUserException;

    boolean updatePassword(long userId, String password);

    boolean updateProfilePicture(long userId, String profileUrl);

}
