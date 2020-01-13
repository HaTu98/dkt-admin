package vn.edu.vnu.uet.dktadmin.rest.model.account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class AccountRequest {
    @Size(min = 6, max = 25)
    private String username;
    @NotNull
    private String email;
    @Size(min = 6, max = 25)
    private String password;
    @Size(min = 6, max = 25)
    private String passwordConfirm;
    private String courses;
}
