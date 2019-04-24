package cat.xlagunas.andrtc.push

class PushNotificationServiceImpl(internal val pushNotificationRepository: PushNotificationRepository) : PushNotificationService {

    override fun sendPush(pushTokenList: List<String>, message: PushMessageData) {
        pushNotificationRepository.sendPush(PushMessage(pushTokenList, message))
    }
}
