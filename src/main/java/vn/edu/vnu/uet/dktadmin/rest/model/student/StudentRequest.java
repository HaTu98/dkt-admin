package vn.edu.vnu.uet.dktadmin.rest.model.student;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class StudentRequest {
    @Size(min = 6, max = 25)
    private String username;
    @NotNull
    private String email;
    @Size(min = 6, max = 25)
    private String password;
    @Size(min = 6, max = 25)
    private String passwordConfirm;
    private String course;
    private String dateOfBirth;
    private String fullName;
    private String sex;
}
