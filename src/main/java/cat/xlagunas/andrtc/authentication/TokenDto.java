package cat.xlagunas.andrtc.authentication;

import java.io.Serializable;

class TokenDto implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    private final String token;

    public TokenDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}
