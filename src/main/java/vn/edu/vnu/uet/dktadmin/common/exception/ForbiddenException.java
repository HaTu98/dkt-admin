package vn.edu.vnu.uet.dktadmin.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForbiddenException extends BaseException {
    private static final long serialVersionUID = 1L;
    private final int code = 403;
    private String message = "Forbidden!";
}
