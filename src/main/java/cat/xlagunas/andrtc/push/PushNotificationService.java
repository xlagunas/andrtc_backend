package cat.xlagunas.andrtc.push;

import java.util.List;

public interface PushNotificationService {

    void sendPush(List<String> pushTokenList, PushMessageData message);
}
