package cat.xlagunas.andrtc.service;

import cat.xlagunas.andrtc.model.Notifiable;

import java.util.List;

public interface PushNotificationService {

    void sendPush(List<Long> receiversList, Notifiable message);

    void sendPush(long receiver, Notifiable message);
}
