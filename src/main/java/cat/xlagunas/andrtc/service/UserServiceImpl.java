package cat.xlagunas.andrtc.service;

import cat.xlagunas.andrtc.exception.ExistingUserException;
import cat.xlagunas.andrtc.exception.UserNotFoundException;
import cat.xlagunas.andrtc.model.UserConverter;
import cat.xlagunas.andrtc.model.UserDto;
import cat.xlagunas.andrtc.repository.UserRepository;
import cat.xlagunas.andrtc.repository.model.User;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.List;


@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto findUser(long idUser) throws UserNotFoundException {
        return UserConverter.convert(userRepository.findUser(idUser));
    }

    @Override
    public UserDto findUser(String username) throws UserNotFoundException {
        return UserConverter.convert(userRepository.findUser(username));
    }

    @Override
    public List<UserDto> searchByUsername(String username) {
        return Lists.transform(userRepository.findUsers(username), new Function<User, UserDto>() {
            @Nullable
            @Override
            public UserDto apply(@Nullable User user) {
                return UserConverter.convert(user);
            }
        });
    }

    @Override
    public void createUser(UserDto userDto) throws ExistingUserException {
        userRepository.insertUser(UserConverter.convert(userDto));
    }

    @Override
    public boolean updatePassword(long userId, String password) {
        return userRepository.updatePassword(userId, password);
    }

    @Override
    public boolean updateProfilePicture(long userId, String profileUrl) {
        return userRepository.updateProfilePic(userId, profileUrl);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return findUser(username);
        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }
}
