package cat.xlagunas.andrtc.push;

import java.util.List;

public class PushNotificationServiceImpl implements PushNotificationService {

    final PushNotificationRepository pushNotificationRepository;

    public PushNotificationServiceImpl(PushNotificationRepository pushNotificationRepository) {
        this.pushNotificationRepository = pushNotificationRepository;
    }

    @Override
    public void sendPush(List<String> pushTokenList, PushMessageData message) {
        pushNotificationRepository.sendPush(new PushMessage.Builder()
                .content(message)
                .tokenList(pushTokenList)
                .build());
    }
}
