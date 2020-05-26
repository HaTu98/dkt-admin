package vn.edu.vnu.uet.dktadmin.rest.model.subjectSemesterExam;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import vn.edu.vnu.uet.dktadmin.rest.model.PageResponse;

import java.util.List;

@Getter
@Setter
public class ListRegisterDetailResponse {
    @JsonProperty(value = "RegisterDetailResponses")
    private List<RegisterDetailResponse> registerDetailResponses;

    @JsonProperty(value = "Page")
    PageResponse pageResponse;

    public List<RegisterDetailResponse> getRegisterDetailResponses() {
        return registerDetailResponses;
    }

    public void setRegisterDetailResponses(List<RegisterDetailResponse> registerDetailResponses) {
        this.registerDetailResponses = registerDetailResponses;
    }

    public PageResponse getPageResponse() {
        return pageResponse;
    }

    public void setPageResponse(PageResponse pageResponse) {
        this.pageResponse = pageResponse;
    }

    @Override
    public String toString() {
        return "ListRegisterDetailResponse{" +
                "registerDetailResponses=" + registerDetailResponses +
                ", pageResponse=" + pageResponse +
                '}';
    }
}
