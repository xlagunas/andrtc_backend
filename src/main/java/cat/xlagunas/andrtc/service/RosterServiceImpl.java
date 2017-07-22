package cat.xlagunas.andrtc.service;

import cat.xlagunas.andrtc.exception.ExistingRelationshipException;
import cat.xlagunas.andrtc.model.FriendDto;
import cat.xlagunas.andrtc.model.FriendshipStatus;
import cat.xlagunas.andrtc.model.RosterConverter;
import cat.xlagunas.andrtc.repository.RosterRepository;
import cat.xlagunas.andrtc.repository.model.Roster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RosterServiceImpl implements RosterService {

    @Autowired
    private final RosterRepository repository;

    public RosterServiceImpl(RosterRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<FriendDto> getAllFriends(long id) {
        return repository.findAll(id).stream()
                .map(RosterConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<FriendDto> filterFriendsByStatus(long id, FriendshipStatus status) {

        return repository.findByStatus(id, status.name()).stream()
                .map(RosterConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getAllFriendshipIds(long userId) {
        return repository.findAllIds(userId);
    }

    @Override
    @Transactional
    public void acceptFriendship(long userId, long ownerId) {
        List<Boolean> rosters = repository.findBothRelationships(userId, ownerId).stream()
                .map(repository::findRosterRelationship)
                .map(RosterHelper::acceptFriendship)
                .map(roster -> repository.updateRelationship(roster.id, roster.relationStatus))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = ExistingRelationshipException.class)
    public void requestFriendship(long userId, long contactId) throws ExistingRelationshipException {
        List<Roster> friendships = Arrays.asList(RosterHelper.createRequestFriendship(userId, contactId),
                RosterHelper.createPendingFriendship(contactId, userId));

        for (Roster roster : friendships) {
            repository.insertRoster(roster);
        }
    }

    @Override
    public void rejectFriendship(long userId, long contactId) {
        List<Long> relationshipsId = repository.findBothRelationships(userId, contactId);
        repository.removeRelationships(relationshipsId);
    }

    @Override
    public void updateFriendshipStatus(long friendshipId, FriendshipStatus status) {
        repository.updateRelationship(friendshipId, status.name());
    }
}
