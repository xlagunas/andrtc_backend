package cat.xlagunas.andrtc.service;

import cat.xlagunas.andrtc.exception.ExistingUserException;
import cat.xlagunas.andrtc.exception.UserNotFoundException;
import cat.xlagunas.andrtc.model.UserDto;
import cat.xlagunas.andrtc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return userRepository.findUser(idUser);
    }

    @Override
    public UserDto findUser(String username) throws UserNotFoundException {
        return userRepository.findUser(username);
    }

    @Override
    public List<UserDto> searchByUsername(String username) {
        return userRepository.findUsers(username);
    }

    @Override
    public void createUser(UserDto userDto) throws ExistingUserException {
        userRepository.insertUser(userDto);
    }

    @Override
    public boolean updatePassword(long userId, String password) {
        UserDto userDto = new UserDto.Builder()
                .id(userId)
                .password(password)
                .build();
        return userRepository.updatePassword(userDto);
    }

    @Override
    public boolean updateProfilePicture(long userId, String profileUrl) {
        UserDto userDto = new UserDto.Builder()
                .id(userId)
                .profilePic(profileUrl)
                .build();
        return userRepository.updatePassword(userDto);
    }
}
