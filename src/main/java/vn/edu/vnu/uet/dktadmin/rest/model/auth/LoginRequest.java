package vn.edu.vnu.uet.dkt.rest.model.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class LoginRequest {
    @Size(min = 6, max = 25)
    private String username;
    @Size(min = 6, max = 25)
    private String password;
}
