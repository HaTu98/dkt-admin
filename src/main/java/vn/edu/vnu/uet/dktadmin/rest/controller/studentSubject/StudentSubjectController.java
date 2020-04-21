package vn.edu.vnu.uet.dktadmin.rest.controller.studentSubject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.vnu.uet.dktadmin.common.exception.BaseException;
import vn.edu.vnu.uet.dktadmin.dto.service.studentSubject.StudentSubjectService;
import vn.edu.vnu.uet.dktadmin.rest.model.ApiDataResponse;
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

}
