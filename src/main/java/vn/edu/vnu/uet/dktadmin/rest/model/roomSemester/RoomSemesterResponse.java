package vn.edu.vnu.uet.dktadmin.rest.model.roomSemester;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomSemesterResponse {
    @JsonProperty(value = "Id")
    private Long id;

    @JsonProperty(value = "NumberOfComputer")
    private int numberOfComputer;

    @JsonProperty(value = "SemesterCode")
    private String semesterCode;

    @JsonProperty(value = "RoomCode")
    private String roomCode;
}
