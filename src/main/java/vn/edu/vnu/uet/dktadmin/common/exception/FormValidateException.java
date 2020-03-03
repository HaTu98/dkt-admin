package vn.edu.vnu.uet.dktadmin.common.exception;

import lombok.*;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormValidateException extends BaseException{

    private static final long serialVersionUID = 1L;
    private int code;
    private String message;
    private Map<String, Object> data;
}