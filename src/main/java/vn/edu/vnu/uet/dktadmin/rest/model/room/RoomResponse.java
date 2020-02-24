package vn.edu.vnu.uet.dktadmin.rest.model.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomResponse {
    @JsonProperty(value = "RoomName")
    private String roomName;

    @JsonProperty(value = "RoomCode")
    private String roomCode;

    @JsonProperty(value = "location")
    private String location;

    @JsonProperty(value = "description")
    private String description;
}
