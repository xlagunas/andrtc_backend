package cat.xlagunas.andrtc.repository.model;

import java.util.Date;

public class Conference {
    public final String callId;
    public final Date date;

    public Conference(String callId, Date date) {
        this.callId = callId;
        this.date = date;
    }
}
