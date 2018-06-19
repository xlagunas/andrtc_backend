package cat.xlagunas.andrtc.service;

import cat.xlagunas.andrtc.exception.ExistingRelationshipException;
import cat.xlagunas.andrtc.model.FriendDto;
import cat.xlagunas.andrtc.model.FriendshipStatus;
import cat.xlagunas.andrtc.model.RosterConverter;
import cat.xlagunas.andrtc.repository.RosterRepository;
import cat.xlagunas.andrtc.repository.model.JoinedRoster;
import cat.xlagunas.andrtc.repository.model.Roster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RosterServiceImpl implements RosterService {

    @Autowired
    private final RosterRepository rosterRepository;

    public RosterServiceImpl(RosterRepository rosterRepository) {
        this.rosterRepository = rosterRepository;
    }

    @Override
    public List<FriendDto> getAllFriends(long id) {
        return rosterRepository.findAll(id).stream()
                .map(RosterConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<FriendDto> filterFriendsByStatus(long id, FriendshipStatus status) {

        return rosterRepository.findByStatus(id, status.name()).stream()
                .map(RosterConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getAllFriendshipIds(long userId) {
        return rosterRepository.findAllIds(userId);
    }

    @Override
    @Transactional
    public void acceptFriendship(long userId, long ownerId) {
        List<Boolean> rosters = rosterRepository.findBothRelationships(userId, ownerId).stream()
                .map(rosterRepository::findRosterRelationship)
                .map(RosterHelper::acceptFriendship)
                .map(roster -> rosterRepository.updateRelationship(roster.id, roster.relationStatus))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = ExistingRelationshipException.class)
    public void requestFriendship(long userId, long contactId) throws ExistingRelationshipException {
        List<Roster> friendships = Arrays.asList(RosterHelper.createRequestFriendship(userId, contactId),
                RosterHelper.createPendingFriendship(contactId, userId));

        for (Roster roster : friendships) {
            rosterRepository.insertRoster(roster);
        }
    }

    @Override
    public void rejectFriendship(long userId, long contactId) {
        List<Long> relationshipsId = rosterRepository.findBothRelationships(userId, contactId);
        rosterRepository.removeRelationships(relationshipsId);
    }

    @Override
    public void updateFriendshipStatus(long friendshipId, FriendshipStatus status) {
        rosterRepository.updateRelationship(friendshipId, status.name());
    }

    @Override
    public List<JoinedRoster> search(long userId, String query) {
        return rosterRepository.findByUsernameOrName(userId, query);
    }
}
