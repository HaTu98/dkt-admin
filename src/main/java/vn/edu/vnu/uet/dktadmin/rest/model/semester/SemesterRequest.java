package vn.edu.vnu.uet.dktadmin.rest.model.semester;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SemesterRequest {
    @NotNull
    @JsonProperty(value = "SemesterName")
    private String semesterName;
    @NotNull
    @JsonProperty(value = "SemesterCode")
    private String semesterCode;

    @JsonProperty(value = "SemesterTitle")
    private String semesterTitle;

    @JsonProperty(value = "Description")
    private String description;
}
