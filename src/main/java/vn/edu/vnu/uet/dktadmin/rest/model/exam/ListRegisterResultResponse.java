package vn.edu.vnu.uet.dktadmin.rest.model.exam;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import vn.edu.vnu.uet.dktadmin.rest.model.PageResponse;

import java.util.List;

@Getter
@Setter
public class ListRegisterResultResponse {
    @JsonProperty(value = "ExamResponses")
    private List<RegisterResultResponse> registerResultResponses;

    @JsonProperty(value = "Page")
    PageResponse pageResponse;

    public ListRegisterResultResponse(List<RegisterResultResponse> registerResultResponses, PageResponse pageResponse) {
        this.registerResultResponses = registerResultResponses;
        this.pageResponse = pageResponse;
    }

    public List<RegisterResultResponse> getRegisterResultResponses() {
        return registerResultResponses;
    }

    public void setRegisterResultResponses(List<RegisterResultResponse> registerResultResponses) {
        this.registerResultResponses = registerResultResponses;
    }

    public PageResponse getPageResponse() {
        return pageResponse;
    }

    public void setPageResponse(PageResponse pageResponse) {
        this.pageResponse = pageResponse;
    }

    @Override
    public String toString() {
        return "ListRegisterResponse{" +
                "registerResponses=" + registerResultResponses +
                ", pageResponse=" + pageResponse +
                '}';
    }
}
