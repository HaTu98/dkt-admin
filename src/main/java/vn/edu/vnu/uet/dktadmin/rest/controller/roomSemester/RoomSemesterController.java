package vn.edu.vnu.uet.dktadmin.rest.controller.roomSemester;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.vnu.uet.dktadmin.dto.service.roomSemester.RoomSemesterService;
import vn.edu.vnu.uet.dktadmin.rest.model.ApiDataResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.roomSemester.RoomSemesterRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.roomSemester.RoomSemesterResponse;

@RestController
@RequestMapping("/admin/room_semesters")
public class RoomSemesterController {

    @Autowired
    private RoomSemesterService roomSemesterService;

    @PostMapping
    public ApiDataResponse<RoomSemesterResponse> createRoomSemester(@RequestBody RoomSemesterRequest request) {

        return ApiDataResponse.ok(roomSemesterService.createRoomSemester(request));
    }


}
