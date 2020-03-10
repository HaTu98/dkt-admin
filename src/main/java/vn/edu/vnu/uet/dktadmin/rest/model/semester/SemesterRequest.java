package vn.edu.vnu.uet.dktadmin.rest.model.semester;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SemesterRequest {
    @JsonProperty(value = "SemesterName")
    private String semesterName;

    @JsonProperty(value = "SemesterId")
    private Long semesterId;

    @JsonProperty(value = "SemesterTitle")
    private String semesterTitle;

    @JsonProperty(value = "Description")
    private String description;
}
