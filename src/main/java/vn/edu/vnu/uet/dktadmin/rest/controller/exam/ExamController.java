package vn.edu.vnu.uet.dktadmin.rest.controller.exam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.vnu.uet.dktadmin.common.exception.FormValidateException;
import vn.edu.vnu.uet.dktadmin.dto.service.exam.ExamService;
import vn.edu.vnu.uet.dktadmin.rest.model.ApiDataResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.exam.ExamRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.exam.ExamResponse;

@RestController
@RequestMapping("/admin")
public class ExamController {
    @Autowired
    private ExamService examService;

    @PostMapping("/exams")
    public ApiDataResponse<ExamResponse> createExam(@RequestBody ExamRequest request) {
        try {
            return ApiDataResponse.ok(examService.create(request));
        } catch (FormValidateException e) {
            return ApiDataResponse.error();
        }
    }

}
