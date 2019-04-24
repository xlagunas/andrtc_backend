package cat.xlagunas.andrtc.push

interface PushNotificationService {
    fun sendPush(pushTokenList: List<String>, message: PushMessageData)
}
