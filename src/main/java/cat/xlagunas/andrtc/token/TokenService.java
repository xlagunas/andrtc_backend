package cat.xlagunas.andrtc.token;

import java.util.List;

public interface TokenService {

    void addToken(Token token) throws ExistingTokenException;

    void removeToke(Token token);

    List<String> getUserToken(long userId);
}
