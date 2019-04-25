package cat.xlagunas.andrtc.token;

import java.util.List;

public interface TokenRepository {

    long addToken(long userId, String token, String platform) throws ExistingTokenException;

    List<Token> getUserTokens(long userId);

    List<Token> getUserListToken(List<Long> userId);

    boolean removeToken(long userId, String token);
}
