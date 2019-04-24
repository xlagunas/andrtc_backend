package cat.xlagunas.andrtc.push

data class PushMessage(val registrationId: List<String>, val data: PushMessageData)
