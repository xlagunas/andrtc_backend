package cat.xlagunas.andrtc.call;

import java.util.Date;

class CallDto {

    final String callId;
    final Date date;

    CallDto(String callId, Date date) {
        this.callId = callId;
        this.date = date;
    }
}
