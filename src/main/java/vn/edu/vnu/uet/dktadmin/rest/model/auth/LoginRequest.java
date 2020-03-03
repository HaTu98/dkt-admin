package vn.edu.vnu.uet.dktadmin.rest.model.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class LoginRequest {
    @JsonProperty(value = "Username")
    private String username;

    @JsonProperty(value = "Password")
    private String password;
}
