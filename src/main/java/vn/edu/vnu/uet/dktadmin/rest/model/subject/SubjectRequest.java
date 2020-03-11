package vn.edu.vnu.uet.dktadmin.rest.model.subject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectRequest {
    @JsonProperty(value = "NumberOfCredit")
    private String numberOfCredit;

    @JsonProperty(value = "SubjectCode")
    private String subjectCode;

    @JsonProperty(value = "SubjectName")
    private String subjectName;
}
