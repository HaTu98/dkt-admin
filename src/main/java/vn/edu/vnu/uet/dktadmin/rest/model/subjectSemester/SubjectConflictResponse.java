package vn.edu.vnu.uet.dktadmin.rest.model.subjectSemester;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SubjectConflictResponse {
    @JsonProperty(value = "SubjectId")
    private Long subjectId;

    @JsonProperty(value = "SubjectConflict")
    private List<Long> subjectConflict;
}
