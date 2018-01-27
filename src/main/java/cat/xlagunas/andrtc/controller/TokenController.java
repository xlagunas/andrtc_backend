package cat.xlagunas.andrtc.controller;

import cat.xlagunas.andrtc.exception.ExistingTokenException;
import cat.xlagunas.andrtc.model.PushTokenDto;
import cat.xlagunas.andrtc.repository.model.Token;
import cat.xlagunas.andrtc.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/token")
public class TokenController {

    @Autowired
    TokenService tokenService;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public void registerToken(UsernamePasswordAuthenticationToken principal, @RequestBody PushTokenDto token) {
        Token validToken = generateValidToken(principal, token);
        try {
            tokenService.addToken(validToken);
        } catch (ExistingTokenException e) {
            new DataIntegrityViolationException("Existing token", e);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public void removeToken(UsernamePasswordAuthenticationToken principal, @RequestBody PushTokenDto token) {
        Token validToken = generateValidToken(principal, token);
        tokenService.removeToke(validToken);
    }

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Data integrity violation")  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void conflict() {
        // Nothing to do
    }

    private Token generateValidToken(UsernamePasswordAuthenticationToken principal, PushTokenDto token) {
        return new Token.Builder()
                .value(token.getValue())
                .userId(AuthenticationUtils.getPrincipalId(principal))
                .platform(token.getPlatform())
                .build();
    }
}
