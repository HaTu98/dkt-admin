package vn.edu.vnu.uet.dktadmin.rest.model.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomResponse {
    @JsonProperty(value = "Id")
    private Long id;

    @JsonProperty(value = "RoomName")
    private String roomName;

    @JsonProperty(value = "RoomCode")
    private String roomCode;

    @JsonProperty(value = "Location")
    private String location;

    @JsonProperty(value = "Description")
    private String description;
}
