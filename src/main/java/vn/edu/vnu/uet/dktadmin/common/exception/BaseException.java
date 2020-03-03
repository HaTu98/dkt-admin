package vn.edu.vnu.uet.dktadmin.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    private int code;
    private String message;
}
