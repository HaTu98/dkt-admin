package vn.edu.vnu.uet.dktadmin.rest.model.subjectSemester;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectSemesterResponse {
    @JsonProperty(value = "Id")
    private Long id;

    @JsonProperty(value = "Description")
    private String description;

    @JsonProperty(value = "SubjectName")
    private String subjectName;

    @JsonProperty(value = "SubjectCode")
    private String subjectCode;

    @JsonProperty(value = "NumberOfCredit")
    private Integer numberOfCredit;

    @JsonProperty(value = "SubjectId")
    private Long subjectId;

    @JsonProperty(value = "SemesterId")
    private Long semesterId;

    @JsonProperty(value = "NumberStudent")
    private Integer numberStudent;
}
