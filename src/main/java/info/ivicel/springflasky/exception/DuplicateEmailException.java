package info.ivicel.springflasky.exception;

public class DuplicateEmailException extends RuntimeException {

    private static final long serialVersionUID = -4238769791839849466L;

    public DuplicateEmailException() {
    }

    public DuplicateEmailException(String message) {
        super(message);
    }

    public DuplicateEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateEmailException(Throwable cause) {
        super(cause);
    }
}
