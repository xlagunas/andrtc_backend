package cat.xlagunas.andrtc.exception;

/**
 * Created by xlagunas on 3/7/17.
 */
public class UserNotFoundException extends Exception {

    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
