package cat.xlagunas.andrtc.call;

import java.util.Date;

import cat.xlagunas.andrtc.model.Notifiable;

public class CallDto implements Notifiable {

    public final String callId;
    public final Date date;

    public CallDto(String callId, Date date) {
        this.callId = callId;
        this.date = date;
    }
}
