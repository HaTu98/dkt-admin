package vn.edu.vnu.uet.dktadmin.rest.controller.subjectSemester;

import org.springframework.web.bind.annotation.*;
import vn.edu.vnu.uet.dktadmin.common.exception.BaseException;
import vn.edu.vnu.uet.dktadmin.dto.service.subjectSemester.SubjectSemesterService;
import vn.edu.vnu.uet.dktadmin.rest.model.ApiDataResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.subjectSemester.SubjectSemesterRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.subjectSemester.SubjectSemesterResponse;

@RestController
@RequestMapping("/admin/subject_semesters")
public class SubjectSemesterController {

    private final SubjectSemesterService subjectSemesterService;

    public SubjectSemesterController(SubjectSemesterService subjectSemesterService) {
        this.subjectSemesterService = subjectSemesterService;
    }

    @PostMapping
    public ApiDataResponse<SubjectSemesterResponse> create(@RequestBody SubjectSemesterRequest request) {
        try {
            return ApiDataResponse.ok(subjectSemesterService.create(request));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @PutMapping
    public ApiDataResponse<SubjectSemesterResponse> update(@RequestBody SubjectSemesterRequest request) {
        try {
            return ApiDataResponse.ok(subjectSemesterService.update(request));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/{id}")
    public ApiDataResponse<SubjectSemesterResponse> getById(@PathVariable Long id) {
        try {
            return ApiDataResponse.ok(subjectSemesterService.getById(id));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

}
