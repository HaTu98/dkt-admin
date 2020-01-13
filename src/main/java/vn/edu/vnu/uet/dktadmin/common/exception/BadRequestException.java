package vn.edu.vnu.uet.dktadmin.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad Request")
public class BadRequestException extends BaseException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String message = "Dữ liệu không hợp lệ";

    public BadRequestException() {

    }

    public BadRequestException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}