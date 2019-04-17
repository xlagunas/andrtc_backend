package cat.xlagunas.andrtc.push;

import java.util.concurrent.CompletableFuture;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PushNotificationRepository {

    @POST("/fcm/send")
    CompletableFuture<ResponseBody> sendPush(@Body PushMessage message);
}
