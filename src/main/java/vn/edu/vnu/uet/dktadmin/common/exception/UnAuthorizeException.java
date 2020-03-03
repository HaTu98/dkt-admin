package vn.edu.vnu.uet.dktadmin.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnAuthorizeException extends BaseException{
    private static final long serialVersionUID = 1L;
    private int code;
    private String message;
}
