package vn.edu.vnu.uet.dktadmin.rest.model.semester;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SemesterResponse {
    @JsonProperty(value = "Id")
    private Long id;

    @JsonProperty(value = "SemesterName")
    private String semesterName;

    @JsonProperty(value = "SemesterCode")
    private String semesterCode;

    @JsonProperty(value = "SemesterTitle")
    private String semesterTitle;

    @JsonProperty(value = "Description")
    private String description;
}
