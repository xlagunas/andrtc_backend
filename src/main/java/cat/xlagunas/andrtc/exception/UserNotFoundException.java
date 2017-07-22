package cat.xlagunas.andrtc.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by xlagunas on 3/7/17.
 */
public class UserNotFoundException extends Exception {

    public UserNotFoundException(){
        super();
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
