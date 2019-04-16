package cat.xlagunas.andrtc.call;

import java.util.Date;

public class Conference {
    public final String callId;
    public final Date date;

    public Conference(String callId, Date date) {
        this.callId = callId;
        this.date = date;
    }
}
