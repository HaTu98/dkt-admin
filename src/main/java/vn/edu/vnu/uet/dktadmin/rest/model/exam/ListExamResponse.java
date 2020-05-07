package vn.edu.vnu.uet.dktadmin.rest.model.exam;

import lombok.Getter;
import lombok.Setter;
import vn.edu.vnu.uet.dktadmin.rest.model.PageResponse;

import java.util.List;

@Getter
@Setter
public class ListExamResponse {
    private List<ExamResponse> examResponses;
    private PageResponse pageResponse;

    public ListExamResponse(List<ExamResponse> examResponses, PageResponse pageResponse) {
        this.examResponses = examResponses;
        this.pageResponse = pageResponse;
    }
}
