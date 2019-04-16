package cat.xlagunas.andrtc.push;

import java.util.List;

public interface PushNotificationService {

    void sendPush(List<Long> receiversList, PushMessageData message);

    void sendPush(long receiver, PushMessageData message);
}
