package vn.edu.vnu.uet.dktadmin.rest.model.roomSemester;

import lombok.Getter;
import lombok.Setter;
import vn.edu.vnu.uet.dktadmin.rest.model.PageResponse;

import java.util.List;

@Getter
@Setter
public class ListRoomSemesterResponse {
    private List<RoomSemesterResponse> roomSemesterResponses;

    private PageResponse pageResponse;

    public ListRoomSemesterResponse(List<RoomSemesterResponse> roomSemesterResponses, PageResponse pageResponse) {
        this.roomSemesterResponses = roomSemesterResponses;
        this.pageResponse = pageResponse;
    }
}
