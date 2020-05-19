package vn.edu.vnu.uet.dktadmin.rest.model.studentSubject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import vn.edu.vnu.uet.dktadmin.rest.model.PageResponse;

import java.util.List;

@Getter
@Setter
public class ListStudentInSubjectResponse {
    @JsonProperty(value = "StudentSubjects")
    List<StudentInSubjectResponse> studentSubjects;

    @JsonProperty(value = "Page")
    private PageResponse pageResponse;

    public ListStudentInSubjectResponse(List<StudentInSubjectResponse> studentSubjects, PageResponse pageResponse) {
        this.studentSubjects = studentSubjects;
        this.pageResponse = pageResponse;
    }
}
