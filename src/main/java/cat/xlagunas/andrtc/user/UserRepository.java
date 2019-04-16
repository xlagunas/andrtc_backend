package cat.xlagunas.andrtc.user;

import cat.xlagunas.andrtc.user.ExistingUserException;
import cat.xlagunas.andrtc.user.UserNotFoundException;
import cat.xlagunas.andrtc.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    long insertUser(User user) throws ExistingUserException;

    User findUser(long userId) throws UserNotFoundException;

    User findUser(String username) throws UserNotFoundException;

    Optional<User> findUserOptional(long userId);

    List<User> findUsers(String username);

    boolean updatePassword(long userId, String password);

    boolean updateProfilePic(long userId, String thumbnail);
}
