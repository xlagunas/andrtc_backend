package cat.xlagunas.andrtc.repository;

import cat.xlagunas.andrtc.exception.ExistingRelationshipException;
import cat.xlagunas.andrtc.model.FriendDto;
import cat.xlagunas.andrtc.model.UserDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RosterRepositoryTest {
    @Autowired
    RosterRepository rosterRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    JdbcTemplate template;

    @Test
    public void whenInsert_thenRosterPersisted() throws ExistingRelationshipException {
        long userId = userRepository.insertUser(UserTestBuilder.getUserWithId(10));
        long contactId = userRepository.insertUser(UserTestBuilder.getUserWithId(20));

        long id = rosterRepository.insertRosterForUser(userRepository.findUserOptional(userId).get(),
                new FriendDto.Builder().id(contactId).status(FriendDto.FriendshipStatus.ACCEPTED).build());

        assert (id > 0);
    }

    @Test(expected = ExistingRelationshipException.class)
    public void givenExistingRelationship_whenInsert_thenErrorInserting() throws ExistingRelationshipException {
        long idUser1 = userRepository.insertUser(UserTestBuilder.getUserWithId(1));
        long idUser2 = userRepository.insertUser(UserTestBuilder.getUserWithId(2));
        long id = rosterRepository.insertRosterForUser(new UserDto.Builder().id((int) idUser1).build(),
                new FriendDto.Builder().id((int) idUser2).status(FriendDto.FriendshipStatus.ACCEPTED).build());

        id = rosterRepository.insertRosterForUser(new UserDto.Builder().id((int) idUser1).build(),
                new FriendDto.Builder().id((int) idUser2).status(FriendDto.FriendshipStatus.REJECTED).build());
    }

    @Test
    public void givenExistingRelationship_whenUpdateStatus_thenSuccess() throws ExistingRelationshipException {
        long idUser1 = userRepository.insertUser(UserTestBuilder.getUserWithId(1));
        long idUser2 = userRepository.insertUser(UserTestBuilder.getUserWithId(2));
        UserDto user1 = new UserDto.Builder().id((int) idUser1).build();
        long id = rosterRepository.insertRosterForUser(user1,
                new FriendDto.Builder().id((int) idUser2).status(FriendDto.FriendshipStatus.ACCEPTED).build());

        rosterRepository.updateRelationship(user1, new FriendDto.Builder().id((int) idUser2).status(FriendDto.FriendshipStatus.REJECTED).build());

        List<FriendDto> friend = rosterRepository.findAll(idUser1);
        assertThat(friend).hasSize(1);
        assertThat(friend.get(0).status).isEqualByComparingTo(FriendDto.FriendshipStatus.REJECTED);
    }

    @Test
    public void givenUserWithRelationships_whenGetAll_thenAllReturned() throws ExistingRelationshipException {
        long idUser1 = userRepository.insertUser(UserTestBuilder.getUserWithId(1));
        UserDto user1 = new UserDto.Builder().id((int) idUser1).build();
        for (int i = 2; i <= 5; i++) {
            long idUser = userRepository.insertUser(UserTestBuilder.getUserWithId(i));
            long id = rosterRepository.insertRosterForUser(user1,
                    new FriendDto.Builder().id((int) idUser).status(FriendDto.FriendshipStatus.ACCEPTED).build());
        }

        List<FriendDto> friendDtoList = rosterRepository.findAll(idUser1);

        assertThat(friendDtoList).hasSize(4);
    }

    @Test
    public void givenUserWithRelationships_whenGetByRelationship_thenReturnFilteredValues() throws ExistingRelationshipException {
        long idUser1 = userRepository.insertUser(UserTestBuilder.getUserWithId(1));
        UserDto user1 = new UserDto.Builder().id((int) idUser1).build();
        for (int i = 2; i <= 6; i++) {
            long idUser = userRepository.insertUser(UserTestBuilder.getUserWithId(i));
            long id = rosterRepository.insertRosterForUser(user1,
                    new FriendDto.Builder().id((int) idUser).status(i % 2 == 0 ? FriendDto.FriendshipStatus.ACCEPTED : FriendDto.FriendshipStatus.PENDING).build());
        }

        List<FriendDto> pendingList = rosterRepository.findByStatus(idUser1, FriendDto.FriendshipStatus.PENDING);
        List<FriendDto> acceptedList = rosterRepository.findByStatus(idUser1, FriendDto.FriendshipStatus.ACCEPTED);
        List<FriendDto> allList = rosterRepository.findAll(idUser1);

        assertThat(pendingList).hasSize(2);
        assertThat(acceptedList).hasSize(3);
        assertThat(allList).hasSize(5);
    }

    private Roster getRoster() {
        return new Roster.Builder()
                .contact(1)
                .owner(4)
                .relationStatus("ACCEPTED")
                .build();
    }

}