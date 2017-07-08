package cat.xlagunas.andrtc.service;

import cat.xlagunas.andrtc.exception.ExistingUserException;
import cat.xlagunas.andrtc.exception.UserNotFoundException;
import cat.xlagunas.andrtc.model.UserDto;

import java.util.List;

public interface UserService {

    UserDto findUser(long idUser) throws UserNotFoundException;

    UserDto findUser(String username) throws UserNotFoundException;

    List<UserDto> searchByUsername(String username);

    void createUser(UserDto userDto) throws ExistingUserException;

    boolean updatePassword(long userId, String password);

    boolean updateProfilePicture(long userId, String profileUrl);

}
