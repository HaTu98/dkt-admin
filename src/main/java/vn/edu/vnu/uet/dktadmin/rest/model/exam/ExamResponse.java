package vn.edu.vnu.uet.dktadmin.rest.model.exam;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ExamResponse {
    @JsonProperty(value = "Id")
    private Integer id;

    @JsonProperty(value = "NumberOfStudent")
    private Integer numberOfStudent;

    @JsonProperty(value = "ExamCode")
    private String examCode;

    @JsonProperty(value = "StartTime")
    private Instant startTime;

    @JsonProperty(value = "Duration")
    private Long duration;

    @JsonProperty(value = "Date")
    private Instant date;

    @JsonProperty(value = "SubjectSemesterId")
    private Long subjectSemesterId;

    @JsonProperty(value = "RoomSemesterId")
    private Long roomSemesterId;
}
