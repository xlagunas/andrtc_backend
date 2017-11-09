package cat.xlagunas.andrtc.model;

import java.util.Date;

public class CallDto implements Notifiable {

    public final String callId;
    public final Date date;

    public CallDto(String callId, Date date) {
        this.callId = callId;
        this.date = date;
    }
}
