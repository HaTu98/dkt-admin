package vn.edu.vnu.uet.dktadmin.rest.controller.exam;

import org.springframework.web.bind.annotation.*;
import vn.edu.vnu.uet.dktadmin.common.exception.BaseException;
import vn.edu.vnu.uet.dktadmin.common.utilities.PageUtil;
import vn.edu.vnu.uet.dktadmin.dto.service.exam.ExamService;
import vn.edu.vnu.uet.dktadmin.rest.model.ApiDataResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.PageBase;
import vn.edu.vnu.uet.dktadmin.rest.model.exam.ExamRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.exam.ExamResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.exam.ListExamResponse;

@RestController
@RequestMapping("/admin")
public class ExamController {
    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping("/exam")
    public ApiDataResponse<ExamResponse> createExam(@RequestBody ExamRequest request) {
        try {
            return ApiDataResponse.ok(examService.create(request));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @PutMapping("/exam")
    public ApiDataResponse<ExamResponse> updateExam(@RequestBody ExamRequest request) {
        try {
            return ApiDataResponse.ok(examService.create(request));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/exam/{id}")
    public ApiDataResponse<ExamResponse> getById(@PathVariable Long id) {
        try {
            return ApiDataResponse.ok(examService.getById(id));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/exam/semester/{id}")
    public ApiDataResponse<ListExamResponse> getExamBySemesterId(
            @PathVariable Long id,
            @RequestParam(required = false, value = "Size") Integer size,
            @RequestParam(required = false, value = "Page") Integer page
    ) {
        try {
            PageBase pageBase = PageUtil.validate(page, size);
            return ApiDataResponse.ok(examService.getBySemesterId(id, pageBase));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

}
