package vn.edu.vnu.uet.dktadmin.rest.model.exam;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterResultResponse {
    @JsonProperty(value = "Id")
    private Long id;

    @JsonProperty(value = "SubjectSemesterId")
    private Long subjectSemesterId;

    @JsonProperty(value = "SubjectName")
    private String subjectName;

    @JsonProperty(value = "SubjectCode")
    private String subjectCode;

    @JsonProperty(value = "NumberOfCredit")
    private Integer numberOfCredit;

    @JsonProperty(value = "RoomName")
    private String roomName;

    @JsonProperty(value = "Location")
    private String location;

    @JsonProperty(value = "NumberOfStudent")
    private Integer numberStudent;

    @JsonProperty(value = "NumberOfStudentSubscribe")
    private Integer numberOfStudentSubscribe;

    @JsonProperty(value = "Date")
    private String date;

    @JsonProperty(value = "Time")
    private String time;
}
