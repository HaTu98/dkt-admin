package vn.edu.vnu.uet.dktadmin.rest.controller.semester;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import vn.edu.vnu.uet.dktadmin.common.exception.BaseException;
import vn.edu.vnu.uet.dktadmin.common.utilities.PageUtil;
import vn.edu.vnu.uet.dktadmin.dto.service.semester.SemesterService;
import vn.edu.vnu.uet.dktadmin.rest.model.ApiDataResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.CheckExistRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.PageBase;
import vn.edu.vnu.uet.dktadmin.rest.model.semester.SemesterListResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.semester.SemesterRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.semester.SemesterResponse;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class SemesterController {
    private static final Logger log = LoggerFactory.getLogger(SemesterController.class);
    private final SemesterService semesterService;

    public SemesterController(SemesterService semesterService) {
        this.semesterService = semesterService;
    }

    @PostMapping("/semester")
    public ApiDataResponse<SemesterResponse> create(@RequestBody SemesterRequest request) {
        try {
            return ApiDataResponse.ok(semesterService.create(request));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @PutMapping("/semester")
    public ApiDataResponse<SemesterResponse> update(@RequestBody SemesterRequest request) {
        try {
            return ApiDataResponse.ok(semesterService.update(request));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @PutMapping("/semester/{id}/active")
    public ApiDataResponse<SemesterResponse> active(@PathVariable Long id) {
        try {
            return ApiDataResponse.ok(semesterService.active(id));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @PutMapping("semester/{id}/done")
    public ApiDataResponse<SemesterResponse>  done(@PathVariable Long id) {
        try {
            return ApiDataResponse.ok(semesterService.done(id));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @PostMapping("/semester/check_exist")
    public ApiDataResponse existStudent (@RequestBody CheckExistRequest checkExistRequest) {
        try {
            log.info("check exist student");
            return ApiDataResponse.ok(semesterService.checkExistSemester(checkExistRequest));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/semester/{id}")
    public ApiDataResponse<SemesterResponse> getSemester(@PathVariable Long id) {
        try {
            return ApiDataResponse.ok(semesterService.getSemesterById(id));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/semester/all")
    public ApiDataResponse<SemesterListResponse> getAll(
            @RequestParam(required = false, value = "Size") Integer size,
            @RequestParam(required = false, value = "Page") Integer page
    ) {
        try {
            PageBase pageBase = PageUtil.validate(page, size);
            return ApiDataResponse.ok(semesterService.getAll(pageBase));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/semester/find")
    public ApiDataResponse<SemesterListResponse> search(
            @RequestParam(value = "Query") String query,
            @RequestParam(required = false, value = "Size") Integer size,
            @RequestParam(required = false, value = "Page") Integer page
    ) {
        try {
            PageBase pageBase = PageUtil.validate(page, size);
            return ApiDataResponse.ok(semesterService.search(query, pageBase));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @DeleteMapping("/semester/list")
    public ApiDataResponse<String> deleteListSemester(@RequestBody List<Long> id) {
        try {
            log.info("delete Room in list: {}", id);
            semesterService.deleteListSemester(id);
            return ApiDataResponse.ok("success");
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }
}
