package cat.xlagunas.andrtc.repository;

import cat.xlagunas.andrtc.exception.ExistingUserException;
import cat.xlagunas.andrtc.model.UserDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Optional;

import static cat.xlagunas.andrtc.repository.UserTestBuilder.getUser;
import static cat.xlagunas.andrtc.repository.UserTestBuilder.getUserWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryImplTest {


    @Autowired
    JdbcTemplate template;

    UserRepository userRepository;

    UserDto user;

    @Before
    public void setUp() {
        user = new UserDto.Builder()
                .username("Ausername")
                .firstname("Auser")
                .lastname("Asurname")
                .email("AnEmail")
                .profilePic("AprofilePic")
                .password("secretPassword")
                .build();

        userRepository = new UserRepositoryImpl(template, new UserRowMapper(), NoOpPasswordEncoder.getInstance());
    }

    @Test
    @Transactional
    @Rollback
    public void whenInsert_thenUserPersisted() throws ExistingUserException {
        long id = userRepository.insertUser(user);

        Optional<UserDto> userDto = userRepository.findUserOptional(id);
        assertThat(userDto.isPresent());
        assertThat(userDto.get()).isEqualToIgnoringGivenFields(user, "id", "passwordUpdate");
    }

    @Test(expected = ExistingUserException.class)
    @Rollback
    @Transactional
    public void givenUsernameAlreadyUsed_whenInsertingUser_thenThrowException() throws ExistingUserException {
        userRepository.insertUser(user);
        userRepository.insertUser(user);
    }

    @Test
    @Rollback
    @Transactional
    public void givenValidUser_whenUpdatePassword_thenUserUpdated() throws ExistingUserException {
        long id = userRepository.insertUser(user);

        boolean state = userRepository
                .updatePassword(getUser((int) id, "newPass", "https://google.com/aPic2"));

        assertTrue(state);
    }

    @Test
    @Rollback
    @Transactional
    public void givenNonValidUser_whenUpdatePassword_thenFailed() {
        boolean state = userRepository
                .updatePassword(getUser(100, "newPass", "https://google.com/aPic2"));

        assertFalse(state);
    }

    @Test
    @Rollback
    @Transactional
    public void whenUpdateProfilePic_thenUserUpdated() throws ExistingUserException {
        long id = userRepository.insertUser(user);

        boolean state = userRepository
                .updateProfilePic(getUser((int) id, "newPass", "https://google.com/aPic1"));

        assertTrue(state);
    }

}