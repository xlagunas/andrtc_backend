package cat.xlagunas.andrtc.model;

public class CallMessageDto implements Notifiable {
    public enum Status {JOINED, REJECTED}

    public final long sender;
    public final Status status;

    public CallMessageDto(long sender, Status status) {
        this.sender = sender;
        this.status = status;
    }
}
