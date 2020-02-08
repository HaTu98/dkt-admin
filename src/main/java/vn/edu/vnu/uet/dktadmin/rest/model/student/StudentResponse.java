package vn.edu.vnu.uet.dktadmin.rest.model.student;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StudentResponse {
    private Long id;
    private String username;
    private String email;
    private String course;
    private String dateOfBirth;
    private String fullName;
    private String sex;
}
