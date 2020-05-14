package vn.edu.vnu.uet.dktadmin.rest.model.subjectSemesterExam;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutoRegisterResponse {
    @JsonProperty("NumberSuccess")
    private Integer numberSuccess;

    @JsonProperty("NumberFail")
    private Integer numberFail;

    public AutoRegisterResponse(Integer numberSuccess, Integer numberFail) {
        this.numberSuccess = numberSuccess;
        this.numberFail = numberFail;
    }
}
