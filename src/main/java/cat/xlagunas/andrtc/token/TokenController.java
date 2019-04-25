package cat.xlagunas.andrtc.token;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cat.xlagunas.andrtc.common.AuthenticationUtils;

@RestController
@RequestMapping(value = "/token")
public class TokenController {

    final TokenService tokenService;

    TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

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
    }

    private Token generateValidToken(UsernamePasswordAuthenticationToken principal, PushTokenDto token) {
        return new Token.Builder()
                .value(token.getValue())
                .userId(AuthenticationUtils.getPrincipalId(principal))
                .platform(token.getPlatform())
                .build();
    }
}
