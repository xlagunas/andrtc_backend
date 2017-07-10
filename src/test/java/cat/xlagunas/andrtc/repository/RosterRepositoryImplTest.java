package cat.xlagunas.andrtc.repository;

import cat.xlagunas.andrtc.exception.ExistingRelationshipException;
import cat.xlagunas.andrtc.exception.ExistingUserException;
import cat.xlagunas.andrtc.model.FriendDto;
import cat.xlagunas.andrtc.model.FriendshipStatus;
import cat.xlagunas.andrtc.model.UserDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RosterRepositoryImplTest {

    @Autowired RosterRepository rosterRepositoryImpl;

    @Autowired UserRepository userRepositoryImpl;

    @Autowired JdbcTemplate template;

    PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();

    @Before
    public void setup() {
        userRepositoryImpl = new UserRepositoryImpl(template, new UserRowMapper(), encoder);
    }

    @Test
    public void whenInsert_thenRosterPersisted() throws ExistingRelationshipException, ExistingUserException {
        long userId = userRepositoryImpl.insertUser(UserTestBuilder.getUserWithId(10));
        long contactId = userRepositoryImpl.insertUser(UserTestBuilder.getUserWithId(20));

        long id = rosterRepositoryImpl.insertRosterForUser(userRepositoryImpl.findUserOptional(userId).get(),
                new FriendDto.Builder().id(contactId).status(FriendshipStatus.ACCEPTED).build());

        assert (id > 0);
    }

    @Test(expected = ExistingRelationshipException.class)
    public void givenExistingRelationship_whenInsert_thenErrorInserting() throws ExistingRelationshipException, ExistingUserException {
        long idUser1 = userRepositoryImpl.insertUser(UserTestBuilder.getUserWithId(1));
        long idUser2 = userRepositoryImpl.insertUser(UserTestBuilder.getUserWithId(2));
        long id = rosterRepositoryImpl.insertRosterForUser(new UserDto.Builder().id((int) idUser1).build(),
                new FriendDto.Builder().id((int) idUser2).status(FriendshipStatus.ACCEPTED).build());

        id = rosterRepositoryImpl.insertRosterForUser(new UserDto.Builder().id((int) idUser1).build(),
                new FriendDto.Builder().id((int) idUser2).status(FriendshipStatus.REJECTED).build());
    }

    @Test
    public void givenExistingRelationship_whenUpdateStatus_thenSuccess() throws ExistingRelationshipException, ExistingUserException {
        long idUser1 = userRepositoryImpl.insertUser(UserTestBuilder.getUserWithId(1));
        long idUser2 = userRepositoryImpl.insertUser(UserTestBuilder.getUserWithId(2));
        UserDto user1 = new UserDto.Builder().id((int) idUser1).build();
        long id = rosterRepositoryImpl.insertRosterForUser(user1,
                new FriendDto.Builder().id((int) idUser2).status(FriendshipStatus.ACCEPTED).build());

        rosterRepositoryImpl.updateRelationship(user1, new FriendDto.Builder().id((int) idUser2).status(FriendshipStatus.REJECTED).build());

        List<FriendDto> friend = rosterRepositoryImpl.findAll(idUser1);
        assertThat(friend).hasSize(1);
        assertThat(friend.get(0).status).isEqualByComparingTo(FriendshipStatus.REJECTED);
    }

    @Test
    public void givenUserWithRelationships_whenGetAll_thenAllReturned() throws ExistingRelationshipException, ExistingUserException {
        long idUser1 = userRepositoryImpl.insertUser(UserTestBuilder.getUserWithId(1));
        UserDto user1 = new UserDto.Builder().id((int) idUser1).build();
        for (int i = 2; i <= 5; i++) {
            long idUser = userRepositoryImpl.insertUser(UserTestBuilder.getUserWithId(i));
            long id = rosterRepositoryImpl.insertRosterForUser(user1,
                    new FriendDto.Builder().id((int) idUser).status(FriendshipStatus.ACCEPTED).build());
        }

        List<FriendDto> friendDtoList = rosterRepositoryImpl.findAll(idUser1);

        assertThat(friendDtoList).hasSize(4);
    }

    @Test
    public void givenUserWithRelationships_whenGetByRelationship_thenReturnFilteredValues() throws ExistingRelationshipException, ExistingUserException {
        long idUser1 = userRepositoryImpl.insertUser(UserTestBuilder.getUserWithId(1));
        UserDto user1 = new UserDto.Builder().id((int) idUser1).build();
        for (int i = 2; i <= 6; i++) {
            long idUser = userRepositoryImpl.insertUser(UserTestBuilder.getUserWithId(i));
            long id = rosterRepositoryImpl.insertRosterForUser(user1,
                    new FriendDto.Builder().id((int) idUser).status(i % 2 == 0 ? FriendshipStatus.ACCEPTED : FriendshipStatus.PENDING).build());
        }

        List<FriendDto> pendingList = rosterRepositoryImpl.findByStatus(idUser1, FriendshipStatus.PENDING);
        List<FriendDto> acceptedList = rosterRepositoryImpl.findByStatus(idUser1, FriendshipStatus.ACCEPTED);
        List<FriendDto> allList = rosterRepositoryImpl.findAll(idUser1);

        assertThat(pendingList).hasSize(2);
        assertThat(acceptedList).hasSize(3);
        assertThat(allList).hasSize(5);
    }

}