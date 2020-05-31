package vn.edu.vnu.uet.dktadmin.rest.model.exam;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ExamRequest {
    @JsonProperty(value = "Id")
    private Long id;

    @JsonProperty(value = "NumberOfStudent")
    private Integer numberOfStudent;

    @JsonProperty(value = "ExamCode")
    private String examCode;

    @JsonProperty(value = "StartTime")
    private String startTime;

    @JsonProperty(value = "EndTime")
    private String endTime;

    @JsonProperty(value = "Date")
    private String date;

    @JsonProperty(value = "SubjectSemesterId")
    private Long subjectSemesterId;

    @JsonProperty(value = "RoomSemesterId")
    private Long roomSemesterId;
}
