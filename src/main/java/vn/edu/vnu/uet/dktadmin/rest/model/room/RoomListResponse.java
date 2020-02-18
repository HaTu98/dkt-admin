package vn.edu.vnu.uet.dktadmin.rest.model.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RoomListResponse {
    List<RoomResponse> rooms;
}
