package cat.xlagunas.andrtc.push;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.concurrent.CompletableFuture;

public interface PushNotificationRepository {

    @POST("/fcm/send")
    public CompletableFuture<ResponseBody> sendPush(@Body PushMessage message);
}
