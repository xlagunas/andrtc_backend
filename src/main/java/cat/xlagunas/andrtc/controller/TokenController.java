package cat.xlagunas.andrtc.controller;

import cat.xlagunas.andrtc.exception.ExistingTokenException;
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
    public void registerToken(UsernamePasswordAuthenticationToken principal, @RequestBody Token token) {
        Token validToken = generateValidToken(principal, token);
        try {
            tokenService.addToken(validToken);
        } catch (ExistingTokenException e) {
            new DataIntegrityViolationException("Existing token", e);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public void removeToken(UsernamePasswordAuthenticationToken principal, @RequestBody Token token) {
        Token validToken = generateValidToken(principal, token);
        tokenService.removeToke(token);
    }

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Data integrity violation")  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void conflict() {
        // Nothing to do
    }

    private Token generateValidToken(UsernamePasswordAuthenticationToken principal, @RequestBody Token token) {
        return new Token.Builder()
                .value(token.value)
                .userId(AuthenticationUtils.getPrincipalId(principal))
                .platform("ANDROID")
                .build();
    }
}
