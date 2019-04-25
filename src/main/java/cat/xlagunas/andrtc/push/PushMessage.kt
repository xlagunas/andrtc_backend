package cat.xlagunas.andrtc.push

import com.fasterxml.jackson.annotation.JsonProperty

data class PushMessage(@JsonProperty("registration_ids") val registrationId: List<String>, @JsonProperty("data") val data: PushMessageData)
