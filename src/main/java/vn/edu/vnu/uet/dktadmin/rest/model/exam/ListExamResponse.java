package vn.edu.vnu.uet.dktadmin.rest.model.exam;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import vn.edu.vnu.uet.dktadmin.rest.model.PageResponse;

import java.util.List;

@Getter
@Setter
public class ListExamResponse {
    @JsonProperty(value = "Exams")
    private List<ExamResponse> examResponses;

    @JsonProperty(value = "Page")
    private PageResponse pageResponse;

    public ListExamResponse(List<ExamResponse> examResponses, PageResponse pageResponse) {
        this.examResponses = examResponses;
        this.pageResponse = pageResponse;
    }
}
