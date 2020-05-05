package vn.edu.vnu.uet.dktadmin.rest.controller.room;


import org.springframework.web.bind.annotation.*;
import vn.edu.vnu.uet.dktadmin.common.exception.BaseException;
import vn.edu.vnu.uet.dktadmin.common.utilities.PageUtil;
import vn.edu.vnu.uet.dktadmin.dto.service.room.RoomService;
import vn.edu.vnu.uet.dktadmin.rest.controller.BaseController;
import vn.edu.vnu.uet.dktadmin.rest.model.ApiDataResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.PageBase;
import vn.edu.vnu.uet.dktadmin.rest.model.room.RoomListResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.room.RoomRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.room.RoomResponse;

@RestController
@RequestMapping("/admin")
public class RoomController extends BaseController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/room")
    public ApiDataResponse<RoomResponse> createRoom(@RequestBody RoomRequest roomRequest) {
        try {
            return ApiDataResponse.ok(roomService.createRoom(roomRequest));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/room")
    public ApiDataResponse<RoomListResponse> getAllRoom(
            @RequestParam(required = false, value = "Size") Integer size,
            @RequestParam(required = false, value = "Page") Integer page
    ) {
        try {
            PageBase pageBase = PageUtil.validate(page, size);
            return ApiDataResponse.ok(roomService.getAllRoom(pageBase));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/room/{id}")
    public ApiDataResponse<RoomResponse> getRoom(@PathVariable Long id) {
        try {
            return ApiDataResponse.ok(roomService.getRoom(id));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @PutMapping("/room")
    public ApiDataResponse<RoomResponse> updateRoom(@RequestBody RoomRequest roomRequest) {
        try {
            return ApiDataResponse.ok(roomService.updateRoom(roomRequest));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/room/find")
    public ApiDataResponse<RoomListResponse> searchRoom(@RequestParam(value = "Query") String query, @RequestParam PageBase pageRequest) {
        try {
            return ApiDataResponse.ok(roomService.searchRoom(query, pageRequest));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }


}
