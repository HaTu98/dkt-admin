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

    @JsonProperty(value = "Description")
    private String description;

    @JsonProperty(value = "StartDate")
    private String startDate;

    @JsonProperty(value = "EndDate")
    private String endDate;

    @JsonProperty(value = "Year")
    private Integer year;

    @JsonProperty(value = "Status")
    private Integer status;
}
