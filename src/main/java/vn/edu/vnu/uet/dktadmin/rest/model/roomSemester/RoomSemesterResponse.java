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
    private Long numberOfComputer;

    @JsonProperty(value = "PreventiveComputer")
    private String preventiveComputer;

    @JsonProperty(value = "RoomSemesterCode")
    private String roomSemesterCode;

    @JsonProperty(value = "SemesterId")
    private Long semesterId;

    @JsonProperty(value = "RoomId")
    private Long roomId;
}
