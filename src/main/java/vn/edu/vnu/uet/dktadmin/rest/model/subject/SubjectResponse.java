package vn.edu.vnu.uet.dktadmin.rest.model.subject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectResponse {
    @JsonProperty(value = "Id")
    private String id;

    @JsonProperty(value = "NumberOfCredit")
    private Integer numberOfCredit;

    @JsonProperty(value = "SubjectCode")
    private String subjectCode;

    @JsonProperty(value = "SubjectName")
    private String subjectName;

    @JsonProperty(value = "Description")
    private String description;
}
