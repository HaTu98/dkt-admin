package vn.edu.vnu.uet.dktadmin.rest.model.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomRequest {
    @JsonProperty(value = "RoomName")
    private String roomName;

    @JsonProperty(value = "RoomId")
    private Long roomId;

    @JsonProperty(value = "LocationId")
    private Long locationId;

    @JsonProperty(value = "Description")
    private String description;
}
