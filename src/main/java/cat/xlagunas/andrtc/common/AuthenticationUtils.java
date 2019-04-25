package cat.xlagunas.andrtc.common;

import cat.xlagunas.andrtc.user.UserDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class AuthenticationUtils {
    public static long getPrincipalId(UsernamePasswordAuthenticationToken principal) {
        UserDto dto = (UserDto) principal.getPrincipal();
        return dto.id;
    }
}
