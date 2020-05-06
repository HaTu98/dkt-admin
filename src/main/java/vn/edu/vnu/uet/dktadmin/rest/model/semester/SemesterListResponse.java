package vn.edu.vnu.uet.dktadmin.rest.model.semester;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import vn.edu.vnu.uet.dktadmin.rest.model.PageResponse;

import java.util.List;

@Getter
@Setter
public class SemesterListResponse {
    @JsonProperty("Semesters")
    List<SemesterResponse> semesterResponses;

    @JsonProperty(value = "Page")
    PageResponse pageResponse;

    public SemesterListResponse (List<SemesterResponse> semesterResponses) {
        this.semesterResponses = semesterResponses;
    }

}
