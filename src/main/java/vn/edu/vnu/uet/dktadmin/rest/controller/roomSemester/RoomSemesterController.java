package vn.edu.vnu.uet.dktadmin.rest.controller.roomSemester;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.vnu.uet.dktadmin.common.exception.BaseException;
import vn.edu.vnu.uet.dktadmin.common.utilities.PageUtil;
import vn.edu.vnu.uet.dktadmin.dto.service.roomSemester.RoomSemesterService;
import vn.edu.vnu.uet.dktadmin.rest.controller.student.StudentController;
import vn.edu.vnu.uet.dktadmin.rest.model.ApiDataResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.PageBase;
import vn.edu.vnu.uet.dktadmin.rest.model.roomSemester.ListRoomSemesterResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.roomSemester.RoomSemesterRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.roomSemester.RoomSemesterResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.roomSemester.UpdateRoomRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin/room_semesters")
public class RoomSemesterController {
    private static final Logger log = LoggerFactory.getLogger(StudentController.class);
    private final RoomSemesterService roomSemesterService;

    public RoomSemesterController(RoomSemesterService roomSemesterService) {
        this.roomSemesterService = roomSemesterService;
    }

    @PostMapping
    public ApiDataResponse<RoomSemesterResponse> createRoomSemester(@RequestBody RoomSemesterRequest request) {
        try {
            log.info("create Room Semester : {}", request);
            return ApiDataResponse.ok(roomSemesterService.createRoomSemester(request));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @PutMapping
    public ApiDataResponse<RoomSemesterResponse> update(@RequestBody RoomSemesterRequest request) {
        try {
            log.info("Update Room Semester : {}", request);
            return ApiDataResponse.ok(roomSemesterService.update(request));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @PutMapping("/list")
    public ApiDataResponse<String> updateList(@RequestBody List<UpdateRoomRequest> requests) {
        try {
            log.info("Update ListRoom Semester : {}", requests);
            roomSemesterService.updateList(requests);
            return ApiDataResponse.ok("success");
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/{id}")
    public ApiDataResponse<RoomSemesterResponse> getById(@PathVariable Long id) {
        try {
            log.info("Update Room Semester  id: {}", id);
            return ApiDataResponse.ok(roomSemesterService.getById(id));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/semester/{id}")
    public ApiDataResponse<ListRoomSemesterResponse> getBySemesterId(
            @PathVariable Long id,
            @RequestParam(required = false, value = "Size") Integer size,
            @RequestParam(required = false, value = "Page") Integer page
    ) {
        try {
            log.info("get Room Semester by semesterId: {}", id);
            PageBase pageBase = PageUtil.validate(page, size);
            return ApiDataResponse.ok(roomSemesterService.getBySemesterId(id,pageBase));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @DeleteMapping("/list")
    public ApiDataResponse<String> deleteListSubjectSemester(@RequestBody List<Long> ids) {
        try {
            log.info("delete roomSemester in list: {}", ids);
            roomSemesterService.deleteListRoomSemester(ids);
            return ApiDataResponse.ok("success");
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @PostMapping("/list")
    public ApiDataResponse<String> createList(@RequestBody List<RoomSemesterRequest> requests) {
        log.info("delete roomSemester in list: {}", requests);
        for (RoomSemesterRequest request : requests) {
            try {
                roomSemesterService.createRoomSemester(request);

            } catch (BaseException e) {
                log.error(e.getMessage());
                return ApiDataResponse.error(e.getCode(), e.getMessage());
            } catch (Exception e) {
                log.error(e.getMessage());
                return ApiDataResponse.error();
            }
        }
        return ApiDataResponse.ok("success");
    }

    @GetMapping("/export/semester/{id}")
    public ResponseEntity<String> export(@PathVariable Long id, HttpServletResponse response) throws IOException {
        log.info("export room semester");
        response.setContentType("application/vnd.ms-excel");
        Workbook workbook = roomSemesterService.export(id);
        String excelFileName = "RoomSemester.xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + excelFileName);
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
        out.close();
        return ResponseEntity.ok().build();
    }

}
