package info.ivicel.springflasky.exception;

public class DuplicateUsernameException extends RuntimeException {

    private static final long serialVersionUID = 3253545764412566406L;

    public DuplicateUsernameException() {
    }

    public DuplicateUsernameException(String message) {
        super(message);
    }

    public DuplicateUsernameException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateUsernameException(Throwable cause) {
        super(cause);
    }
}
