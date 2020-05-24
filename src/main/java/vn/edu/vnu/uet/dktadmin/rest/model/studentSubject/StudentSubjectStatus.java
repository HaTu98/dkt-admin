package vn.edu.vnu.uet.dktadmin.rest.model.studentSubject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentSubjectStatus {
    @JsonProperty(value = "Id")
    private Long id;

    @JsonProperty(value = "Status")
    private Integer status;
}
