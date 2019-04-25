package cat.xlagunas.andrtc.push

import cat.xlagunas.andrtc.common.MessageType
import com.fasterxml.jackson.annotation.JsonProperty

data class PushMessageData(@JsonProperty("eventType") val eventType: MessageType, @JsonProperty("params") val params: Map<String, Any> = emptyMap())