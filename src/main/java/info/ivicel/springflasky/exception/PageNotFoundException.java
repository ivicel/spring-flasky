package info.ivicel.springflasky.exception;

public class PageNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -3056488160960008790L;

    public PageNotFoundException() {
    }

    public PageNotFoundException(String message) {
        super(message);
    }

    public PageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PageNotFoundException(Throwable cause) {
        super(cause);
    }
}
