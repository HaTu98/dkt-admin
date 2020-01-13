package vn.edu.vnu.uet.dktadmin.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ResponseStatus(value = HttpStatus.OK, reason = "Item not found")
public class NotFoundException extends BaseException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    protected Map<String, Object> messageResult;

    private String message = "Không tìm thấy đối tượng";
    public NotFoundException() {

    }

    public NotFoundException(Map<String, Object> messageResult) {
        this.messageResult = messageResult;
    }

    public NotFoundException(String message){
        this.message=message;
    }
    @Override
    public String getMessage(){
        return this.message;
    }
}