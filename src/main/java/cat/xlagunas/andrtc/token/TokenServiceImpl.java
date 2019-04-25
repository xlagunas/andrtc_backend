package cat.xlagunas.andrtc.token;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;

public class TokenServiceImpl implements TokenService {

    @Autowired
    TokenRepository tokenRepository;

    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void addToken(Token token) throws ExistingTokenException {
        tokenRepository.addToken(token.userId, token.value, token.platform);
    }

    @Override
    public void removeToke(Token token) {
        tokenRepository.removeToken(token.userId, token.value);
    }

    @Override
    public List<String> getUsersToken(long userId) {
        return Lists.transform(tokenRepository.getUserTokens(userId), token -> token.value);
    }

    @Override
    public List<String> getUsersToken(List<Long> userIdList) {
        return userIdList.stream()
                .map(this::getUsersToken).flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
