package cat.xlagunas.andrtc.user;

/**
 * Created by xlagunas on 7/7/17.
 */
public class ExistingUserException extends Exception {

    public ExistingUserException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
