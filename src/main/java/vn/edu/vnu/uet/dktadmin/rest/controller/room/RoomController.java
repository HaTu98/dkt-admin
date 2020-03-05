package vn.edu.vnu.uet.dktadmin.rest.controller.room;


import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.edu.vnu.uet.dktadmin.common.exception.FormValidateException;
import vn.edu.vnu.uet.dktadmin.dto.service.room.RoomService;
import vn.edu.vnu.uet.dktadmin.rest.controller.BaseController;
import vn.edu.vnu.uet.dktadmin.rest.model.ApiDataResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.auth.LoginResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.room.RoomListResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.room.RoomRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.room.RoomResponse;

@RestController
@RequestMapping("/admin/rooms")
public class RoomController extends BaseController {

    @Autowired
    private RoomService roomService;

    @PostMapping
//    @ApiResponse(content = @Content(mediaType = "text/plain",
//            schema = @Schema(implementation= LoginResponse.class)))
    public ApiDataResponse<RoomResponse> createRoom(@RequestBody RoomRequest roomRequest) {
        try {
            return ApiDataResponse.ok(roomService.createRoom(roomRequest));
        }catch (FormValidateException e){
            return ApiDataResponse.error(e.getData(),e.getCode(), e.getMessage());
        }
    }

    @GetMapping
    public ApiDataResponse<RoomListResponse> getAllRoom() {
        return ApiDataResponse.ok(roomService.getAllRoom());
    }

}
