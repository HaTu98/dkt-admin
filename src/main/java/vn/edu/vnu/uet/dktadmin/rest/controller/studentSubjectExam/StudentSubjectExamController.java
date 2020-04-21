package vn.edu.vnu.uet.dktadmin.rest.controller.studentSubjectExam;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.vnu.uet.dktadmin.common.exception.BaseException;
import vn.edu.vnu.uet.dktadmin.dto.service.studentSubjectExam.StudentSubjectExamService;
import vn.edu.vnu.uet.dktadmin.rest.model.ApiDataResponse;
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
}
