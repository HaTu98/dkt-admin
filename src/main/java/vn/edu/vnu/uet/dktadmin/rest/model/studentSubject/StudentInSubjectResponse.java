package vn.edu.vnu.uet.dktadmin.rest.model.studentSubject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentInSubjectResponse {
    @JsonProperty(value = "Id")
    private Long studentId;

    @JsonProperty(value = "SubjectSemesterId")
    private Long subjectSemesterId;

    @JsonProperty(value = "StudentSubjectId")
    private Long studentSubjectId;

    @JsonProperty(value = "StudentName")
    private String studentName;

    @JsonProperty(value = "StudentCode")
    private String studentCode;

    @JsonProperty(value = "Gender")
    private Integer studentGender;

    @JsonProperty(value = "Email")
    private String email;

    @JsonProperty(value = "DateOfBirth")
    private String studentDateOfBirth;

    @JsonProperty(value = "Course")
    private String course;

    @JsonProperty(value = "Status")
    private Integer status;
}
