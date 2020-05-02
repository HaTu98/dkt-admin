package vn.edu.vnu.uet.dktadmin.rest.controller.room;


import org.springframework.web.bind.annotation.*;
import vn.edu.vnu.uet.dktadmin.common.exception.BaseException;
import vn.edu.vnu.uet.dktadmin.dto.service.room.RoomService;
import vn.edu.vnu.uet.dktadmin.rest.controller.BaseController;
import vn.edu.vnu.uet.dktadmin.rest.model.ApiDataResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.room.RoomListResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.room.RoomRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.room.RoomResponse;

@RestController
@RequestMapping("/admin/rooms")
public class RoomController extends BaseController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public ApiDataResponse<RoomResponse> createRoom(@RequestBody RoomRequest roomRequest) {
        try {
            return ApiDataResponse.ok(roomService.createRoom(roomRequest));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @GetMapping
    public ApiDataResponse<RoomListResponse> getAllRoom() {
        return ApiDataResponse.ok(roomService.getAllRoom());
    }

}
