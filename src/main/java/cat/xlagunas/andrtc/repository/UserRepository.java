package cat.xlagunas.andrtc.repository;

import cat.xlagunas.andrtc.exception.ExistingUserException;
import cat.xlagunas.andrtc.exception.UserNotFoundException;
import cat.xlagunas.andrtc.model.UserDto;

import java.util.List;
import java.util.Optional;

/**
 * Created by xlagunas on 7/7/17.
 */
public interface UserRepository {
    long insertUser(UserDto user) throws ExistingUserException;

    UserDto findUser(long userId) throws UserNotFoundException;

    Optional<UserDto> findUserOptional(long userId);

    List<UserDto> findUsers(String username);

    boolean updatePassword(UserDto user);

    boolean updateProfilePic(UserDto user);
}
