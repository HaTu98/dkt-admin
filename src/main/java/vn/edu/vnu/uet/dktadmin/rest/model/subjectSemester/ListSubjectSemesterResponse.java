package vn.edu.vnu.uet.dktadmin.rest.model.subjectSemester;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import vn.edu.vnu.uet.dktadmin.rest.model.PageResponse;

import java.util.List;

@Getter
@Setter
public class ListSubjectSemesterResponse {
    List<SubjectSemesterResponse> subjectSemesterResponses;
    @JsonProperty(value = "Page")
    PageResponse pageResponse;

    public ListSubjectSemesterResponse(List<SubjectSemesterResponse> subjectSemesterResponses, PageResponse pageResponse) {
        this.subjectSemesterResponses = subjectSemesterResponses;
        this.pageResponse = pageResponse;
    }
}
