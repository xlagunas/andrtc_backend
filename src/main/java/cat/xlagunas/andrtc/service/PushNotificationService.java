package cat.xlagunas.andrtc.service;

import cat.xlagunas.andrtc.repository.model.PushMessageData;

import java.util.List;

public interface PushNotificationService {

    void sendPush(List<Long> receiversList, PushMessageData message);

    void sendPush(long receiver, PushMessageData message);
}
