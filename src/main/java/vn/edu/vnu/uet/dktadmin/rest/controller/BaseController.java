package vn.edu.vnu.uet.dktadmin.rest.controller;

import com.google.common.base.CaseFormat;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ServerErrorException;
import vn.edu.vnu.uet.dktadmin.common.exception.BadRequestException;
import vn.edu.vnu.uet.dktadmin.common.exception.ForbiddenException;
import vn.edu.vnu.uet.dktadmin.common.exception.FormValidateException;
import vn.edu.vnu.uet.dktadmin.common.exception.UnauthorizedException;
import vn.edu.vnu.uet.dktadmin.common.validator.CustomError;
import vn.edu.vnu.uet.dktadmin.common.validator.ValidateMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class BaseController {
    @ExceptionHandler(FormValidateException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public ValidateMessage handleFormValidateException(FormValidateException ex, HttpServletRequest request) {
        val exceptionMessage = new ValidateMessage();
        Map<String, Object> errors = new HashMap<>();
        exceptionMessage.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        if (ex.getBindingResult() != null) {
            for (val error : ex.getBindingResult().getFieldErrors()) {
                putFieldError(errors, error);
            }
        }
        if (ex.getMessageResult() != null) {
            for (val error : ex.getMessageResult().entrySet()) {
                val key = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, error.getKey());
                val value = error.getValue();
                if (!errors.containsKey(key)) {
                    errors.put(key, value);
                }
            }
        }
        exceptionMessage.setErrors(errors);
        return exceptionMessage;
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public CustomError handleForbiddenException(HttpServletResponse response, ForbiddenException exception) {
        response.setContentType("application/json");
        return new CustomError(exception.getMessage());

    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public CustomError handleUnauthorizedException(HttpServletResponse response) {
        response.setContentType("application/json");
        return new CustomError("Thông tin xác thực không chính xác");
    }

    @ExceptionHandler(ServerErrorException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public CustomError handleServerErrorException(HttpServletResponse response, ServerErrorException exception) {
        response.setContentType("application/json");
        return new CustomError(exception.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public CustomError handleBadRequestException(HttpServletResponse response, BadRequestException exception) {
        response.setContentType("application/json");
        return new CustomError(exception.getMessage());
    }

//    @ExceptionHandler(NotFoundException.class)
//    @ResponseStatus(value = HttpStatus.NOT_FOUND)
//    public CustomError handleNotFoundException(HttpServletResponse response, NotFoundException e) {
//        response.setContentType("application/json");
//        response.setHeader("Content-Type", "application/json");
//        //response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//        return new CustomError(e.getMessage());
//    }

    private void putFieldError(Map<String, Object> errors, FieldError error) {
        val key = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, error.getField());
        val value = error.getDefaultMessage();
        if (!errors.containsKey(key)) {
            errors.put(key, value.toString());
        }
    }


}
