package vn.edu.vnu.uet.dktadmin.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "SERVER ERROR")
public class ServerErrorException extends BaseException{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String message = "Đã có lỗi xảy ra";
    public ServerErrorException(){

    }
    public ServerErrorException(String message){
        this.message = message;
    }
    @Override
    public String getMessage(){
        return this.message;
    }
}
