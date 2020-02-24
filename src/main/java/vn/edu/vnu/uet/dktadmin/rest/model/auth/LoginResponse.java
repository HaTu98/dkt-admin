package vn.edu.vnu.uet.dkt.rest.model.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginResponse {
    @JsonProperty(value = "Token")
    private String token;
}
