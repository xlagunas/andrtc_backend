package cat.xlagunas.andrtc.service;

import cat.xlagunas.andrtc.exception.ExistingTokenException;
import cat.xlagunas.andrtc.repository.model.Token;

import java.util.List;

public interface TokenService {

    void addToken(Token token) throws ExistingTokenException;

    void removeToke(Token token);

    List<String> getUserToken(long userId);
}
