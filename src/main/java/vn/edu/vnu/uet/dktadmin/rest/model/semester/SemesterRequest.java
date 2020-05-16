package vn.edu.vnu.uet.dktadmin.rest.model.semester;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SemesterRequest {
    @JsonProperty(value = "Id")
    private Long id;

    @JsonProperty(value = "SemesterName")
    private String semesterName;

    @JsonProperty(value = "SemesterCode")
    private String semesterCode;

    @JsonProperty(value = "StartDate")
    private String startDate;

    @JsonProperty(value = "EndDate")
    private String endDate;

    @JsonProperty(value = "Year")
    private Integer year;

    @JsonProperty(value = "Description")
    private String description;
}
