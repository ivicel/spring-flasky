package info.ivicel.springflasky.exception;

public class AccountExistsException extends RuntimeException {

    private static final long serialVersionUID = 1694176761236873378L;

    public AccountExistsException() {
    }

    public AccountExistsException(String message) {
        super(message);
    }

    public AccountExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountExistsException(Throwable cause) {
        super(cause);
    }
}
