package vn.edu.vnu.uet.dktadmin.rest.model.studentSubject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentSubjectRequest {
    @JsonProperty("Id")
    private Long id;

    @JsonProperty(value = "StudentId")
    private Long studentId;

    @JsonProperty(value = "StudentCode")
    private String studentCode;

    @JsonProperty(value = "SubjectSemesterId")
    private Long subjectSemesterId;
}
