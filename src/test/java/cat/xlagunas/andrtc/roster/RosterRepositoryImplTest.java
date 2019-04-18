package cat.xlagunas.andrtc.roster;

import cat.xlagunas.andrtc.user.UserRepository;
import cat.xlagunas.andrtc.user.UserRepositoryImpl;
import cat.xlagunas.andrtc.user.UserTestBuilder;
import cat.xlagunas.andrtc.user.ExistingUserException;
import cat.xlagunas.andrtc.user.UserDto;
import cat.xlagunas.andrtc.common.UserRowMapper;

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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RosterRepositoryImplTest {

    @Autowired
    RosterRepository rosterRepositoryImpl;

    @Autowired
    UserRepository userRepositoryImpl;

    @Autowired
    JdbcTemplate template;

    PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();

    @Test
    @Transactional
    @Rollback
    public void whenInsert_thenRosterPersisted() throws ExistingRelationshipException, ExistingUserException {
        long userId = userRepositoryImpl.insertUser(UserTestBuilder.getUserWithId(10));
        long contactId = userRepositoryImpl.insertUser(UserTestBuilder.getUserWithId(20));
        Roster roster = new Roster.Builder().contact(contactId).owner(userId)
                .relationStatus(FriendshipStatus.ACCEPTED.name()).build();

        long id = rosterRepositoryImpl.insertRoster(roster);

        assert (id > 0);
    }

    @Test(expected = ExistingRelationshipException.class)
    @Transactional
    @Rollback
    public void givenExistingRelationship_whenInsert_thenErrorInserting() throws ExistingRelationshipException, ExistingUserException {
        long idUser1 = userRepositoryImpl.insertUser(UserTestBuilder.getUserWithId(1));
        long idUser2 = userRepositoryImpl.insertUser(UserTestBuilder.getUserWithId(2));
        Roster roster = new Roster.Builder().contact(idUser2).owner(idUser1)
                .relationStatus(FriendshipStatus.ACCEPTED.name()).build();
        long id = rosterRepositoryImpl.insertRoster(roster);

        id = rosterRepositoryImpl.insertRoster(new Roster.Builder().contact(idUser2).owner(idUser1)
                .relationStatus(FriendshipStatus.REJECTED.name()).build());
    }

    @Test
    @Transactional
    @Rollback
    public void givenExistingRelationship_whenUpdateStatus_thenSuccess() throws ExistingRelationshipException, ExistingUserException {
        long idUser1 = userRepositoryImpl.insertUser(UserTestBuilder.getUserWithId(1));
        long idUser2 = userRepositoryImpl.insertUser(UserTestBuilder.getUserWithId(2));
        long id = rosterRepositoryImpl.insertRoster(new Roster.Builder().contact(idUser2).owner(idUser1)
                .relationStatus(FriendshipStatus.ACCEPTED.name()).build());

        rosterRepositoryImpl.updateRelationship(id, FriendshipStatus.REJECTED.name());

        List<JoinedRoster> friend = rosterRepositoryImpl.findAll(idUser1);
        assertThat(friend).hasSize(1);
        assertThat(friend.get(0).status).isEqualTo(FriendshipStatus.REJECTED.name());
    }

    @Test
    @Transactional
    @Rollback
    public void givenUserWithRelationships_whenGetAll_thenAllReturned() throws ExistingRelationshipException, ExistingUserException {
        long idUser1 = userRepositoryImpl.insertUser(UserTestBuilder.getUserWithId(1));
        for (int i = 2; i <= 5; i++) {
            long idContact = userRepositoryImpl.insertUser(UserTestBuilder.getUserWithId(i));
            rosterRepositoryImpl.insertRoster(new Roster.Builder()
                    .owner(idUser1)
                    .contact(idContact)
                    .relationStatus(FriendshipStatus.ACCEPTED.name()).build());
        }

        List<JoinedRoster> friendDtoList = rosterRepositoryImpl.findAll(idUser1);

        assertThat(friendDtoList).hasSize(4);
    }

    @Test
    @Transactional
    @Rollback
    public void givenUserWithRelationships_whenGetByRelationship_thenReturnFilteredValues() throws ExistingRelationshipException, ExistingUserException {
        long idUser1 = userRepositoryImpl.insertUser(UserTestBuilder.getUserWithId(1));
        UserDto user1 = new UserDto.Builder().id((int) idUser1).build();
        for (int i = 2; i <= 6; i++) {
            long idUser = userRepositoryImpl.insertUser(UserTestBuilder.getUserWithId(i));
            long id = rosterRepositoryImpl.insertRoster(new Roster.Builder()
                    .owner(idUser1)
                    .contact(idUser)
                    .relationStatus(i % 2 == 0 ? FriendshipStatus.ACCEPTED.name() : FriendshipStatus.PENDING.name()).build());
        }

        List<JoinedRoster> pendingList = rosterRepositoryImpl.findByStatus(idUser1, FriendshipStatus.PENDING.name());
        List<JoinedRoster> acceptedList = rosterRepositoryImpl.findByStatus(idUser1, FriendshipStatus.ACCEPTED.name());
        List<JoinedRoster> allList = rosterRepositoryImpl.findAll(idUser1);

        assertThat(pendingList).hasSize(2);
        assertThat(acceptedList).hasSize(3);
        assertThat(allList).hasSize(5);
    }

    @Test
    @Transactional
    @Rollback
    public void givenExistingRelationship_whenQueryById_thenReturnRoster() throws Exception {
        Roster expected = insertAndReturnOne();

        Roster roster = rosterRepositoryImpl.findRosterRelationship(expected.id);

        assertThat(expected).isEqualToComparingFieldByField(roster);
    }

    private Roster insertAndReturnOne() throws Exception {
        long userId = userRepositoryImpl.insertUser(UserTestBuilder.getUser());
        long contactId = userRepositoryImpl.insertUser(UserTestBuilder.getUser());

        long id = rosterRepositoryImpl.insertRoster(
                new Roster.Builder()
                        .owner(userId)
                        .contact(contactId)
                        .relationStatus(FriendshipStatus.ACCEPTED.name())
                        .build());

        return new Roster.Builder()
                .id(id)
                .contact(contactId)
                .owner(userId)
                .relationStatus(FriendshipStatus.ACCEPTED.name())
                .build();
    }

}