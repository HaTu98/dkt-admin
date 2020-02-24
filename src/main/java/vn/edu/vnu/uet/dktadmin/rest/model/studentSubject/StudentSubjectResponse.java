package vn.edu.vnu.uet.dktadmin.rest.model.studentSubject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentSubjectResponse {
    @JsonProperty(value = "StudentCode")
    private String studentCode;

    @JsonProperty(value = "SubjectCode")
    private String subjectCode;

    @JsonProperty(value = "SemesterCode")
    private String semesterCode;

    @JsonProperty(value = "status")
    private String status;
}
