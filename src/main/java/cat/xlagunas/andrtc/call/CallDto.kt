package cat.xlagunas.andrtc.call

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Date

data class CallDto(@JsonProperty("callId") val callId: String, @JsonProperty("date") val date: Date)
