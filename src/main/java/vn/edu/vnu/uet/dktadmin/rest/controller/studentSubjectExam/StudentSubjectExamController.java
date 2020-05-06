package vn.edu.vnu.uet.dktadmin.rest.controller.studentSubjectExam;

import org.springframework.web.bind.annotation.*;
import vn.edu.vnu.uet.dktadmin.common.exception.BaseException;
import vn.edu.vnu.uet.dktadmin.common.utilities.PageUtil;
import vn.edu.vnu.uet.dktadmin.dto.model.StudentSubjectExam;
import vn.edu.vnu.uet.dktadmin.dto.service.studentSubjectExam.StudentSubjectExamService;
import vn.edu.vnu.uet.dktadmin.rest.model.ApiDataResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.PageBase;
import vn.edu.vnu.uet.dktadmin.rest.model.subjectSemesterExam.ListStudentSubjectExamResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.subjectSemesterExam.StudentSubjectExamRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.subjectSemesterExam.StudentSubjectExamResponse;

@RestController
@RequestMapping("/admin/student_subject_exam")
public class StudentSubjectExamController {
    private final StudentSubjectExamService studentSubjectExamService;

    public StudentSubjectExamController(StudentSubjectExamService studentSubjectExamService) {
        this.studentSubjectExamService = studentSubjectExamService;
    }

    @PostMapping
    public ApiDataResponse<StudentSubjectExamResponse> create(@RequestBody StudentSubjectExamRequest request) {
        try {
            return ApiDataResponse.ok(studentSubjectExamService.create(request));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/semester/{id}")
    public ApiDataResponse<ListStudentSubjectExamResponse> getBySemesterId(
            @PathVariable Long id,
            @RequestParam(required = false, value = "Size") Integer size,
            @RequestParam(required = false, value = "Page") Integer page
    ) {
        try {
            PageBase pageBase = PageUtil.validate(page, size);
            return ApiDataResponse.ok(studentSubjectExamService.getBySemester(id, pageBase));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/subject_semester/{id}")
    public ApiDataResponse<ListStudentSubjectExamResponse> getByStudentSubject(
            @PathVariable Long id,
            @RequestParam(required = false, value = "Size") Integer size,
            @RequestParam(required = false, value = "Page") Integer page
    ) {
        try {
            PageBase pageBase = PageUtil.validate(page, size);
            return ApiDataResponse.ok(studentSubjectExamService.getBySubjectSemester(id, pageBase));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/{id}")
    public ApiDataResponse<StudentSubjectExamResponse> get(@PathVariable Long id) {
        try {
            return ApiDataResponse.ok(studentSubjectExamService.getById(id));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

}
