package vn.edu.vnu.uet.dktadmin.common.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class DktStudent {
    private Long id;
    private String username;
    private String email;
    private String courses;
    private String role;
}
