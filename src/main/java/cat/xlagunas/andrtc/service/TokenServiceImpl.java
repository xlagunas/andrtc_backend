package cat.xlagunas.andrtc.service;

import cat.xlagunas.andrtc.exception.ExistingTokenException;
import cat.xlagunas.andrtc.repository.TokenRepository;
import cat.xlagunas.andrtc.repository.model.Token;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
    public List<String> getUserToken(long userId) {
        return Lists.transform(tokenRepository.getUserTokens(userId), token -> token.value);
    }
}
