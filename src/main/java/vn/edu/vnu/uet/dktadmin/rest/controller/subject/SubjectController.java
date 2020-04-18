package vn.edu.vnu.uet.dktadmin.rest.controller.subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.vnu.uet.dktadmin.common.exception.BaseException;
import vn.edu.vnu.uet.dktadmin.common.exception.FormValidateException;
import vn.edu.vnu.uet.dktadmin.dto.service.subject.SubjectService;
import vn.edu.vnu.uet.dktadmin.rest.model.ApiDataResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.subject.SubjectRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.subject.SubjectResponse;

@RequestMapping("/admin")
@RestController
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @PostMapping("/subject")
    public ApiDataResponse<SubjectResponse> createSubject(@RequestBody SubjectRequest request) {
        try {
            return ApiDataResponse.ok(subjectService.createSubject(request));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        }
    }

}
