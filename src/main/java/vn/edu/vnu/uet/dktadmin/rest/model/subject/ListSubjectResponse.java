package vn.edu.vnu.uet.dktadmin.rest.model.subject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import vn.edu.vnu.uet.dktadmin.rest.model.PageResponse;

import java.util.List;

@Getter
@Setter
public class ListSubjectResponse {
    @JsonProperty("SubjectResponse")
    List<SubjectResponse> subjectResponses;

    @JsonProperty(value = "Page")
    PageResponse pageResponse;

    public ListSubjectResponse (List<SubjectResponse> subjectResponses) {
        this.subjectResponses = subjectResponses;
    }
}
