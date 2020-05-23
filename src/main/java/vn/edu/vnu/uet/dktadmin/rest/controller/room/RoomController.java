package vn.edu.vnu.uet.dktadmin.rest.controller.room;


import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnu.uet.dktadmin.common.exception.BaseException;
import vn.edu.vnu.uet.dktadmin.common.utilities.ExcelUtil;
import vn.edu.vnu.uet.dktadmin.common.utilities.PageUtil;
import vn.edu.vnu.uet.dktadmin.dto.service.room.RoomService;
import vn.edu.vnu.uet.dktadmin.rest.controller.BaseController;
import vn.edu.vnu.uet.dktadmin.rest.controller.exam.ExamController;
import vn.edu.vnu.uet.dktadmin.rest.model.ApiDataResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.CheckExistRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.PageBase;
import vn.edu.vnu.uet.dktadmin.rest.model.room.RoomListResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.room.RoomRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.room.RoomResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.subject.ListSubjectResponse;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class RoomController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(ExamController.class);
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/room")
    public ApiDataResponse<RoomResponse> createRoom(@RequestBody RoomRequest roomRequest) {
        try {
            log.info("create room {}", roomRequest);
            return ApiDataResponse.ok(roomService.createRoom(roomRequest));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/room/all")
    public ApiDataResponse<RoomListResponse> getAllRoom(
            @RequestParam(required = false, value = "Size") Integer size,
            @RequestParam(required = false, value = "Page") Integer page
    ) {
        try {
            log.info("get all room");
            PageBase pageBase = PageUtil.validate(page, size);
            return ApiDataResponse.ok(roomService.getAllRoom(pageBase));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/room/{id}")
    public ApiDataResponse<RoomResponse> getRoom(@PathVariable Long id) {
        try {
            log.info("get room by id {}", id);
            return ApiDataResponse.ok(roomService.getRoom(id));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @PutMapping("/room")
    public ApiDataResponse<RoomResponse> updateRoom(@RequestBody RoomRequest roomRequest) {
        try {
            log.info("update room by : {}", roomRequest );
            return ApiDataResponse.ok(roomService.updateRoom(roomRequest));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/room/find")
    public ApiDataResponse<RoomListResponse> searchRoom(
            @RequestParam(value = "Query") String query,
            @RequestParam(required = false, value = "Size") Integer size,
            @RequestParam(required = false, value = "Page") Integer page
    ) {
        try {
            log.info("find room  query : {}", query);
            PageBase pageBase = PageUtil.validate(page, size);
            return ApiDataResponse.ok(roomService.searchRoom(query, pageBase));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @PostMapping("/room/check_exist")
    public ApiDataResponse existStudent (@RequestBody CheckExistRequest checkExistRequest) {
        try {
            log.info("check exist Room");
            return ApiDataResponse.ok(roomService.checkExistRoom(checkExistRequest));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @DeleteMapping("/room/list")
    public ApiDataResponse<String> deleteListRoom(@RequestBody List<Long> id) {
        try {
            log.info("delete Room in list: {}", id);
            roomService.deleteListRoom(id);
            return ApiDataResponse.ok("success");
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/room/template")
    public ResponseEntity<?> template(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        XSSFWorkbook xssfWorkbook = roomService.template();
        String excelFileName = "Template_Room.xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + excelFileName);
        ServletOutputStream out = response.getOutputStream();
        xssfWorkbook.write(out);
        out.flush();
        out.close();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/room/import")
    public ResponseEntity<?> importRoom(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        List<XSSFRow> errors = roomService.importRoom(file);
        if (errors.size() > 0) {
            Workbook fileErrors = roomService.template();
            Sheet sheetErrors = fileErrors.getSheetAt(0);
            for (int i = 0 ; i < errors.size(); i++) {
                Row rowOld = errors.get(i);
                Row rowNew = sheetErrors.createRow(5 + i);
                ExcelUtil.copyRow(rowNew, rowOld);
            }
            response.setContentType("application/vnd.ms-excel");
            String excelFileName = "Errors_Room.xlsx";
            response.setHeader("Content-Disposition", "attachment; filename=" + excelFileName);
            ServletOutputStream out = response.getOutputStream();
            fileErrors.write(out);
            out.flush();
            out.close();
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.ok("success");
    }

    @GetMapping("/room_not_in_semester/{id}")
    public ApiDataResponse<ListSubjectResponse> subjectNotInSemester(
            @PathVariable Long id,
            @RequestParam(required = false, value = "Size") Integer size,
            @RequestParam(required = false, value = "Page") Integer page
    ) {
        try {
            log.info("get room not in semester all");
            PageBase pageBase = PageUtil.validate(page, size);
            return ApiDataResponse.ok(roomService.getRoomNotInSemester(id, pageBase));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }
}
