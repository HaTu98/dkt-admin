package vn.edu.vnu.uet.dktadmin.rest.model.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.edu.vnu.uet.dktadmin.rest.model.PageResponse;

import java.util.List;

@Getter
@Setter
public class RoomListResponse {
    @JsonProperty(value = "Rooms")
    List<RoomResponse> rooms;

    @JsonProperty(value = "Page")
    PageResponse pageResponse;

    public RoomListResponse(List<RoomResponse> rooms,PageResponse pageResponse) {
        this.rooms = rooms;
        this.pageResponse = pageResponse;
    }
}
