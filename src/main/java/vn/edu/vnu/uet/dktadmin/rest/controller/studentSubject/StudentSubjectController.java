package vn.edu.vnu.uet.dktadmin.rest.controller.studentSubject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.vnu.uet.dktadmin.common.exception.FormValidateException;
import vn.edu.vnu.uet.dktadmin.dto.service.studentSubject.StudentSubjectService;
import vn.edu.vnu.uet.dktadmin.rest.model.studentSubject.StudentSubjectRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.studentSubject.StudentSubjectResponse;

@RestController
@RequestMapping("/admin/student-subjects")
public class StudentSubjectController {

    @Autowired
    private StudentSubjectService studentSubjectService;

    @PostMapping()
    public StudentSubjectResponse create(@RequestBody StudentSubjectRequest request, BindingResult result) {
        if (result.hasErrors()) {
            throw new FormValidateException(result);
        }
        return studentSubjectService.create(request);
    }

}
