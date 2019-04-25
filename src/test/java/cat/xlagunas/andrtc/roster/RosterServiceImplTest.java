package cat.xlagunas.andrtc.roster;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import cat.xlagunas.andrtc.user.UserRepository;
import cat.xlagunas.andrtc.user.UserTestBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RosterServiceImplTest {

    private static final int TOTAL_FRIENDS = 100;
    private static final int TOTAL_FRIENDS_TO_FILTER = 10;

    @Autowired
    JdbcTemplate template;

    @Autowired
    RosterRepository rosterRepository;

    @Autowired
    UserRepository userRepository;

    RosterService rosterService;
    long ownerId;
    long userId;

    @Before
    public void setUp() throws Exception {
        ownerId = userRepository.insertUser(UserTestBuilder.getUser());
        userId = userRepository.insertUser(UserTestBuilder.getUser());

        rosterService = new RosterServiceImpl(rosterRepository);
    }

    @Test
    @Transactional
    @Rollback
    public void givenUserHasFriends_whenGetAllFriends_thenReturnAll() throws Exception {
        for (int i = 0; i < TOTAL_FRIENDS; i++) {
            long userId = userRepository.insertUser(UserTestBuilder.getUser(0, "aPass" + i, "aPic"));
            rosterService.requestFriendship(ownerId, userId);
        }

        assertThat(rosterService.getAllFriends(ownerId), hasSize(TOTAL_FRIENDS));
    }

    @Test
    @Transactional
    @Rollback
    public void whenFilterByRelationship_thenReturnOnlyMatchingRelationship() throws Exception {
        for (int i = 0; i < TOTAL_FRIENDS_TO_FILTER; i++) {
            long userId = userRepository.insertUser(UserTestBuilder.getUser(0, "aPass" + i, "aPic"));
            rosterService.requestFriendship(ownerId, userId);
            if (i % 2 == 0) {
                rosterService.acceptFriendship(ownerId, userId);
            }
        }

        assertThat(rosterService.filterFriendsByStatus(ownerId, FriendshipStatus.ACCEPTED), hasSize(TOTAL_FRIENDS_TO_FILTER / 2));
    }

    @Test
    @Transactional
    @Rollback
    public void whenAcceptFriendship_thenRelationshipUpdated() throws Exception {
        rosterService.requestFriendship(ownerId, userId);

        rosterService.acceptFriendship(ownerId, userId);

        assertThat(rosterService.filterFriendsByStatus(ownerId, FriendshipStatus.ACCEPTED), hasSize(1));
        assertThat(rosterService.filterFriendsByStatus(userId, FriendshipStatus.ACCEPTED), hasSize(1));
    }

    @Test
    @Transactional
    @Rollback
    public void whenRequestFriendship_thenBothRelationshipsCreated() throws Exception {
        rosterService.requestFriendship(ownerId, userId);

        assertThat(rosterService.getAllFriends(ownerId), hasSize(1));
        assertThat(rosterService.getAllFriends(userId), hasSize(1));

        assertThat(rosterService.getAllFriends(ownerId).get(0).status, equalTo(FriendshipStatus.REQUESTED));
        assertThat(rosterService.getAllFriends(userId).get(0).status, equalTo(FriendshipStatus.PENDING));
    }

    @Test
    @Transactional
    @Rollback
    public void whenRejectFriendship_thenBothRelationshipsRemoved() throws Exception {
        rosterService.requestFriendship(ownerId, userId);

        rosterService.rejectFriendship(ownerId, userId);

        assertThat(rosterService.getAllFriends(ownerId), empty());
        assertThat(rosterService.getAllFriends(userId), empty());

    }

    @Test
    @Transactional
    @Rollback
    public void whenUpdatedRelationship_thenStatusChanged() throws Exception {
        rosterService.requestFriendship(ownerId, userId);
        rosterService.acceptFriendship(ownerId, userId);

        List<Long> rels = rosterService.getAllFriendshipIds(ownerId);
        assertThat(rels, not(empty()));
        rosterService.updateFriendshipStatus(rels.get(0), FriendshipStatus.BLOCKED);

        assertThat(rosterService.filterFriendsByStatus(ownerId, FriendshipStatus.BLOCKED), hasSize(1));
    }

}