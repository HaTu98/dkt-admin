package vn.edu.vnu.uet.dktadmin.rest.controller.roomSemester;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.vnu.uet.dktadmin.common.exception.BaseException;
import vn.edu.vnu.uet.dktadmin.dto.service.roomSemester.RoomSemesterService;
import vn.edu.vnu.uet.dktadmin.rest.model.ApiDataResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.roomSemester.RoomSemesterRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.roomSemester.RoomSemesterResponse;

@RestController
@RequestMapping("/admin/room_semesters")
public class RoomSemesterController {
    private final RoomSemesterService roomSemesterService;

    public RoomSemesterController(RoomSemesterService roomSemesterService) {
        this.roomSemesterService = roomSemesterService;
    }

    @PostMapping
    public ApiDataResponse<RoomSemesterResponse> createRoomSemester(@RequestBody RoomSemesterRequest request) {
        try {
            return ApiDataResponse.ok(roomSemesterService.createRoomSemester(request));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }


}
