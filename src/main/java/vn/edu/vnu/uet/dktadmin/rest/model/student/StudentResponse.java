package vn.edu.vnu.uet.dktadmin.rest.model.student;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StudentResponse {
    @JsonProperty(value = "Id")
    private Long id;

    @JsonProperty(value = "UserName")
    private String username;

    @JsonProperty(value = "StudentCode")
    private String studentCode;

    @JsonProperty(value = "Email")
    private String email;

    @JsonProperty(value = "Course")
    private String course;

    @JsonProperty(value = "DateOfBirth")
    private String dateOfBirth;

    @JsonProperty(value = "FullName")
    private String fullName;

    @JsonProperty(value = "Gender")
    private String gender;
}
