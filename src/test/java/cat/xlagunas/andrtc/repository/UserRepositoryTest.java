package cat.xlagunas.andrtc.repository;

import cat.xlagunas.andrtc.exception.UserNotFoundException;
import cat.xlagunas.andrtc.model.UserDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static cat.xlagunas.andrtc.repository.UserTestBuilder.getUser;
import static cat.xlagunas.andrtc.repository.UserTestBuilder.getUserWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JdbcTemplate template;

    @Test
    public void whenInsert_thenUserPersisted() {
        UserDto user = new UserDto.Builder()
                .firstname("Auser")
                .lastname("Asurname")
                .email("AnEmail")
                .profilePic("AprofilePic")
                .password("secretPassword")
                .build();
        long id = userRepository.insertUser(user);

        Optional<UserDto> userDto = userRepository.findUserOptional(id);
        assertThat(userDto.isPresent());
        assertThat(userDto.get()).isEqualToIgnoringGivenFields(user, "id");
    }

    @Test
    public void givenValidUser_whenUpdatePassword_thenUserUpdated() {
        long id = userRepository.insertUser(getUserWithId(100));

        boolean state = userRepository
                .updatePassword(getUser((int) id, "newPass", "https://google.com/aPic2"));

        assertTrue(state);
    }

    @Test
    public void givenNonValidUser_whenUpdatePassword_thenFailed() {
        boolean state = userRepository
                .updatePassword(getUser(100, "newPass", "https://google.com/aPic2"));

        assertFalse(state);
    }

    @Test
    public void whenUpdateProfilePic_thenUserUpdated() {
        long id = userRepository.insertUser(getUserWithId(100));

        boolean state = userRepository
                .updateProfilePic(getUser((int) id, "newPass", "https://google.com/aPic1"));

        assertTrue(state);
    }


}