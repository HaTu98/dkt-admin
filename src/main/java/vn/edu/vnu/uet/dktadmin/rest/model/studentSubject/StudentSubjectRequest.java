package vn.edu.vnu.uet.dktadmin.rest.model.studentSubject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class StudentSubjectRequest {
    @NotNull
    @JsonProperty(value = "StudentCode")
    private String studentCode;

    @NotNull
    @JsonProperty(value = "SubjectCode")
    private String subjectCode;

    @NotNull
    @JsonProperty(value = "SemesterCode")
    private String semesterCode;
}
