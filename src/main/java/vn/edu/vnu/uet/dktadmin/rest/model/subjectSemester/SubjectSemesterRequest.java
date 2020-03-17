package vn.edu.vnu.uet.dktadmin.rest.model.subjectSemester;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectSemesterRequest {
    @JsonProperty(value = "Code")
    private String code;

    @JsonProperty(value = "Description")
    private String description;

    @JsonProperty(value = "SubjectId")
    private Long subjectId;

    @JsonProperty(value = "SemesterId")
    private Long semesterId;
}
