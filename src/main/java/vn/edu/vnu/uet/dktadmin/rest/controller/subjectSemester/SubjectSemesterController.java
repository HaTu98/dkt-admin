package vn.edu.vnu.uet.dktadmin.rest.controller.subjectSemester;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.vnu.uet.dktadmin.common.exception.FormValidateException;
import vn.edu.vnu.uet.dktadmin.dto.service.subjectSemester.SubjectSemesterService;
import vn.edu.vnu.uet.dktadmin.rest.model.ApiDataResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.subjectSemester.SubjectSemesterRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.subjectSemester.SubjectSemesterResponse;

@RestController
@RequestMapping("/admin//subject-semesters")
public class SubjectSemesterController {

    @Autowired
    private SubjectSemesterService subjectSemesterService;

    @PostMapping
    private ApiDataResponse<SubjectSemesterResponse> create(@RequestBody SubjectSemesterRequest request) {
        try {
            return ApiDataResponse.ok(subjectSemesterService.create(request));
        }catch (FormValidateException e) {
            return ApiDataResponse.error(e.getData(),e.getCode(), e.getMessage());
        }
    }
}
