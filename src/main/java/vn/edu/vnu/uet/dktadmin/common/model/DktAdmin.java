package vn.edu.vnu.uet.dktadmin.common.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DktAdmin {
    private Long id;
    private String username;
    private String email;
}
