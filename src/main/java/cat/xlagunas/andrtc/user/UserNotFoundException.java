package cat.xlagunas.andrtc.user;

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
