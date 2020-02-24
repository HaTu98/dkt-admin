package vn.edu.vnu.uet.dktadmin.rest.controller.studentSubject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.vnu.uet.dktadmin.dto.service.studentSubject.StudentSubjectService;

@RestController
@RequestMapping("/admin/student-subjects")
public class StudentSubjectController {

    @Autowired
    private StudentSubjectService studentSubjectService;



}
