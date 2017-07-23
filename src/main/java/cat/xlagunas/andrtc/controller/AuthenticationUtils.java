package cat.xlagunas.andrtc.controller;

import cat.xlagunas.andrtc.model.UserDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class AuthenticationUtils {
    public static long getPrincipalId(UsernamePasswordAuthenticationToken principal) {
        UserDto dto = (UserDto) principal.getPrincipal();
        return dto.id;
    }
}
