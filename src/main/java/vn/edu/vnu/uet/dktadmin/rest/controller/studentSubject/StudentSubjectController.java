package vn.edu.vnu.uet.dktadmin.rest.controller.studentSubject;

import org.springframework.web.bind.annotation.*;
import vn.edu.vnu.uet.dktadmin.common.exception.BaseException;
import vn.edu.vnu.uet.dktadmin.common.utilities.PageUtil;
import vn.edu.vnu.uet.dktadmin.dto.service.studentSubject.StudentSubjectService;
import vn.edu.vnu.uet.dktadmin.rest.model.ApiDataResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.PageBase;
import vn.edu.vnu.uet.dktadmin.rest.model.studentSubject.ListStudentSubjectResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.studentSubject.StudentSubjectRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.studentSubject.StudentSubjectResponse;

@RestController
@RequestMapping("/admin/student_subjects")
public class StudentSubjectController {

    private final StudentSubjectService studentSubjectService;

    public StudentSubjectController(StudentSubjectService studentSubjectService) {
        this.studentSubjectService = studentSubjectService;
    }

    @PostMapping()
    public ApiDataResponse<StudentSubjectResponse> create(@RequestBody StudentSubjectRequest request) {
        try {
            StudentSubjectResponse response = studentSubjectService.create(request);
            return ApiDataResponse.ok(response);
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @PutMapping()
    public ApiDataResponse<StudentSubjectResponse> update(@RequestBody StudentSubjectRequest request) {
        try {
            StudentSubjectResponse response = studentSubjectService.update(request);
            return ApiDataResponse.ok(response);
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/subject_semester/{id}")
    public ApiDataResponse<ListStudentSubjectResponse> getBySubjectSemester(
            @PathVariable Long id,
            @RequestParam(required = false, value = "Size") Integer size,
            @RequestParam(required = false, value = "Page") Integer page
    ) {
        try {
            PageBase pageBase = PageUtil.validate(page, size);
            ListStudentSubjectResponse response = studentSubjectService.getBySubjectSemesterId(id,pageBase);
            return ApiDataResponse.ok(response);
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/semester/{id}")
    public ApiDataResponse<ListStudentSubjectResponse> getBySemester(
            @PathVariable Long id,
            @RequestParam(required = false, value = "Size") Integer size,
            @RequestParam(required = false, value = "Page") Integer page
    ) {
        try {
            PageBase pageBase = PageUtil.validate(page, size);
            ListStudentSubjectResponse response = studentSubjectService.getBySemesterId(id,pageBase);
            return ApiDataResponse.ok(response);
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @DeleteMapping("/{id}")
    public ApiDataResponse<String> delete(@PathVariable Long id) {
        try {
            studentSubjectService.delete(id);
            return ApiDataResponse.ok("success");
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/{id}")
    public ApiDataResponse<StudentSubjectResponse> getById(@PathVariable Long id) {
        try {
            return ApiDataResponse.ok(studentSubjectService.getById(id));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

}
