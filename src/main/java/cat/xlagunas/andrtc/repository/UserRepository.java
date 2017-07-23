package cat.xlagunas.andrtc.repository;

import cat.xlagunas.andrtc.exception.ExistingUserException;
import cat.xlagunas.andrtc.exception.UserNotFoundException;
import cat.xlagunas.andrtc.repository.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    long insertUser(User user) throws ExistingUserException;

    User findUser(long userId) throws UserNotFoundException;

    User findUser(String username) throws UserNotFoundException;

    Optional<User> findUserOptional(long userId);

    List<User> findUsers(String username);

    boolean updatePassword(User user);

    boolean updateProfilePic(User user);
}
