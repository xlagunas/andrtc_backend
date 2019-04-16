package cat.xlagunas.andrtc.token;

public class PushTokenDto {

    private String value;
    private String platform;

    public PushTokenDto() {
    }

    public PushTokenDto(String value, String platform) {
        this.value = value;
        this.platform = platform;
    }

    public String getValue() {
        return value;
    }

    public String getPlatform() {
        return platform;
    }
}
