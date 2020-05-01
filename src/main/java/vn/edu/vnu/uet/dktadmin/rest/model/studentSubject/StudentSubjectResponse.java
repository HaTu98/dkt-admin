package vn.edu.vnu.uet.dktadmin.rest.model.studentSubject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentSubjectResponse {
    @JsonProperty(value = "SubjectId")
    private Long studentId;

    @JsonProperty(value = "SubjectSemesterId")
    private Long subjectSemesterId;

    @JsonProperty(value = "Status")
    private String status;
}
