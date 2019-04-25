package cat.xlagunas.andrtc.user;

import cat.xlagunas.andrtc.user.UserNotFoundException;
import cat.xlagunas.andrtc.user.UserConverter;
import cat.xlagunas.andrtc.user.UserDto;
import cat.xlagunas.andrtc.user.UserRepository;
import cat.xlagunas.andrtc.user.UserService;
import cat.xlagunas.andrtc.user.UserServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class UserServiceImplTest {
    @Mock
    UserRepository userRepository;

    UserService userService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    public void givenExistingUser_whenFindUser_thenReturnUser() throws UserNotFoundException {
        UserDto userDto = new UserDto.Builder()
                .username("aUsername")
                .password("aPassword")
                .build();
        when(userRepository.findUser(anyLong())).thenReturn(UserConverter.convert(userDto));

        UserDto responseDto = userService.findUser(10);

        assertThat(responseDto).isEqualToComparingFieldByField(userDto);
    }

    @Test(expected = UserNotFoundException.class)
    public void givenNonExistingUser_whenFindUser_thenReturnUser() throws UserNotFoundException {
        UserDto userDto = new UserDto.Builder()
                .username("aUsername")
                .password("aPassword")
                .build();
        when(userRepository.findUser(anyLong())).thenThrow(new UserNotFoundException());

        UserDto responseDto = userService.findUser(10);

        assertThat(responseDto).isEqualToComparingFieldByField(userDto);
    }

    @Test
    public void searchByUsername() throws Exception {
    }

    @Test
    public void createUser() throws Exception {
    }

    @Test
    public void updatePassword() throws Exception {
    }

    @Test
    public void updateProfilePicture() throws Exception {
    }

}