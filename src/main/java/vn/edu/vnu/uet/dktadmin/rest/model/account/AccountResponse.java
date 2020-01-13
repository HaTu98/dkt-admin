package vn.edu.vnu.uet.dktadmin.rest.model.account;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccountResponse {
    private Long id;
    private String username;
    private String email;
    private String courses;
}
