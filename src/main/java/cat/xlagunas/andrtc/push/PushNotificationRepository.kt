package cat.xlagunas.andrtc.push

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.CompletableFuture

interface PushNotificationRepository {
    @POST("/fcm/send")
    fun sendPush(@Body message: PushMessage): CompletableFuture<ResponseBody>
}
