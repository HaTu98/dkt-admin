package vn.edu.vnu.uet.dktadmin.common.exception;

@SuppressWarnings("serial")
public class BaseException extends RuntimeException {

    public BaseException(String message) {
        super(message);
    }

    public BaseException() {
        super("Exception");
    }
}
