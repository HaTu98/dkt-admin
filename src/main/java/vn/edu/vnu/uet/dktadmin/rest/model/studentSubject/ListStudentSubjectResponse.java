package vn.edu.vnu.uet.dktadmin.rest.model.studentSubject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import vn.edu.vnu.uet.dktadmin.rest.model.PageResponse;

import java.util.List;

@Getter
@Setter
public class ListStudentSubjectResponse {
    @JsonProperty(value = "StudentSubjects")
    private List<StudentSubjectResponse> studentSubjectResponses;

    @JsonProperty(value = "Page")
    private PageResponse pageResponse;

    public ListStudentSubjectResponse(List<StudentSubjectResponse> studentSubjectResponses, PageResponse pageResponse) {
        this.studentSubjectResponses = studentSubjectResponses;
        this.pageResponse = pageResponse;
    }

}
