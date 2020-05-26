package vn.edu.vnu.uet.dktadmin.rest.controller.exam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import vn.edu.vnu.uet.dktadmin.common.exception.BaseException;
import vn.edu.vnu.uet.dktadmin.common.utilities.PageUtil;
import vn.edu.vnu.uet.dktadmin.dto.service.exam.ExamService;
import vn.edu.vnu.uet.dktadmin.rest.model.ApiDataResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.PageBase;
import vn.edu.vnu.uet.dktadmin.rest.model.exam.*;

@RestController
@RequestMapping("/admin")
public class ExamController {
    private static final Logger log = LoggerFactory.getLogger(ExamController.class);
    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping("/exam")
    public ApiDataResponse<ExamResponse> createExam(@RequestBody ExamRequest request) {
        try {
            log.info("create exam {}", request);
            return ApiDataResponse.ok(examService.create(request));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @PutMapping("/exam")
    public ApiDataResponse<ExamResponse> updateExam(@RequestBody ExamRequest request) {
        try {
            log.info("update exam {}", request);
            return ApiDataResponse.ok(examService.create(request));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/exam/{id}")
    public ApiDataResponse<ExamResponse> getById(@PathVariable Long id) {
        try {
            log.info("get exam by id : {}", id );
            return ApiDataResponse.ok(examService.getById(id));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/exam/semester/{id}")
    public ApiDataResponse<ListRegisterResultResponse> getExamBySemesterId(
            @PathVariable Long id,
            @RequestParam(required = false, value = "Size") Integer size,
            @RequestParam(required = false, value = "Page") Integer page
    ) {
        try {
            log.info("get exam in semester : {}" , id);
            PageBase pageBase = PageUtil.validate(page, size);
            return ApiDataResponse.ok(examService.getBySemesterId(id, pageBase));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

}
