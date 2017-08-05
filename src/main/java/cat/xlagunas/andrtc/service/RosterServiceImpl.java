package cat.xlagunas.andrtc.service;

import cat.xlagunas.andrtc.exception.ExistingRelationshipException;
import cat.xlagunas.andrtc.model.FriendDto;
import cat.xlagunas.andrtc.model.FriendshipStatus;
import cat.xlagunas.andrtc.model.RosterConverter;
import cat.xlagunas.andrtc.repository.PushNotificationRepository;
import cat.xlagunas.andrtc.repository.RosterRepository;
import cat.xlagunas.andrtc.repository.TokenRepository;
import cat.xlagunas.andrtc.repository.model.PushMessage;
import cat.xlagunas.andrtc.repository.model.Roster;
import cat.xlagunas.andrtc.repository.model.Token;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import retrofit2.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class RosterServiceImpl implements RosterService {

    @Autowired
    private final RosterRepository rosterRepository;

    @Autowired
    private final TokenRepository tokenRepository;

    @Autowired
    private final PushNotificationRepository pushNotificationRepository;

    public RosterServiceImpl(RosterRepository rosterRepository, TokenRepository tokenRepository, PushNotificationRepository pushNotificationRepository) {
        this.rosterRepository = rosterRepository;
        this.tokenRepository = tokenRepository;
        this.pushNotificationRepository = pushNotificationRepository;
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

        notifyUpdateContact(ownerId, userId, FriendshipStatus.ACCEPTED);

    }

    private void notifyUpdateContact(long ownerId, long userId, FriendshipStatus relationshipStatus) {
        List<Token> tokenList = tokenRepository.getUserTokens(ownerId);
        List<String> tokenStringList = Lists.transform(tokenList, token -> token.value);
        if (!tokenStringList.isEmpty()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> map = new HashMap<>();
                map.put("key", "value");
                String jsonString = mapper.writeValueAsString(map);
                JsonNode actualObj = mapper.readTree(jsonString);
                PushMessage pushMessage = new PushMessage.Builder()
                        .tokenList(tokenStringList)
                        .content(actualObj)
                        .build();
                ResponseBody response = pushNotificationRepository.sendPush(pushMessage).get();
                System.out.println(response.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
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
}
