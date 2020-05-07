package vn.edu.vnu.uet.dktadmin.rest.model.roomSemester;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomSemesterRequest {
    @JsonProperty(value = "Id")
    private Long id;

    @JsonProperty(value = "NumberOfComputer")
    private Integer numberOfComputer;

    @JsonProperty(value = "AvailableComputer")
    private Integer availableComputer;

    @JsonProperty(value = "RoomSemesterCode")
    private String roomSemesterCode;

    @JsonProperty(value = "SemesterId")
    private Long semesterId;

    @JsonProperty(value = "RoomId")
    private Long roomId;
}
