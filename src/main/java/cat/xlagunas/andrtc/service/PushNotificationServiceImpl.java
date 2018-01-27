package cat.xlagunas.andrtc.service;

import cat.xlagunas.andrtc.repository.PushNotificationRepository;
import cat.xlagunas.andrtc.repository.TokenRepository;
import cat.xlagunas.andrtc.repository.model.PushMessage;
import cat.xlagunas.andrtc.repository.model.PushMessageData;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class PushNotificationServiceImpl implements PushNotificationService {

    @Autowired
    PushNotificationRepository pushNotificationRepository;

    @Autowired
    TokenRepository tokenRepository;

    public PushNotificationServiceImpl(PushNotificationRepository pushNotificationRepository, TokenRepository tokenRepository) {
        this.pushNotificationRepository = pushNotificationRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void sendPush(List<Long> receiversList, PushMessageData message) {
        List<String> userTokens = tokenRepository.getUserListToken(receiversList)
                .stream().map(token -> token.value).collect(Collectors.toList());

        pushNotificationRepository.sendPush(new PushMessage.Builder()
                .content(message)
                .tokenList(userTokens)
                .build());
    }

    @Override
    public void sendPush(long receiver, PushMessageData message) {
        sendPush(Lists.newArrayList(receiver), message);
    }
}
