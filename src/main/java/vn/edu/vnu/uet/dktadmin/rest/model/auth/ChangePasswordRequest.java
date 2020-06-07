package vn.edu.vnu.uet.dktadmin.rest.model.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    @JsonProperty(value = "Password")
    private String password;
    @JsonProperty(value = "PasswordNew")
    private String passwordNew;
    @JsonProperty(value = "PasswordConfirm")
    private String passwordConfirm;
}
