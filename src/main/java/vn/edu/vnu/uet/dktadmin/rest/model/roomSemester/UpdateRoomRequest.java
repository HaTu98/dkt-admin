package vn.edu.vnu.uet.dktadmin.rest.model.roomSemester;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRoomRequest {
    @JsonProperty(value = "Id")
    private Long id;

    @JsonProperty(value = "NumberOfComputer")
    private Integer numberOfComputer;

    @JsonProperty(value = "PreventiveComputer")
    private Integer preventiveComputer;
}
