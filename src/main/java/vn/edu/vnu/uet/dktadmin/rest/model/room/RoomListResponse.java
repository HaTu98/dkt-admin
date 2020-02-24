package vn.edu.vnu.uet.dktadmin.rest.model.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RoomListResponse {
    @JsonProperty(value = "Rooms")
    List<RoomResponse> rooms;
}
