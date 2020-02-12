package vn.edu.vnu.uet.dktadmin.rest.controller.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnu.uet.dktadmin.common.exception.FormValidateException;
import vn.edu.vnu.uet.dktadmin.common.security.AccountService;
import vn.edu.vnu.uet.dktadmin.common.validator.EmailValidator;
import vn.edu.vnu.uet.dktadmin.dto.dao.student.StudentDao;
import vn.edu.vnu.uet.dktadmin.dto.model.Student;
import vn.edu.vnu.uet.dktadmin.dto.service.student.StudentService;
import vn.edu.vnu.uet.dktadmin.rest.controller.BaseController;
import vn.edu.vnu.uet.dktadmin.rest.model.student.StudentListResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.student.StudentRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.student.StudentResponse;

import javax.validation.Valid;
import java.io.IOException;

@RequestMapping("/admin/generation")
@RestController
public class StudentController extends BaseController {
    @Autowired
    private StudentDao studentDao;

    @Autowired
    private EmailValidator emailValidator;

    @Autowired
    private StudentService studentService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountService accountService;

    @PostMapping("/student")
    public StudentResponse createAccount(@RequestBody @Valid StudentRequest request, BindingResult result) {
        if (result.hasErrors()) {
            throw new FormValidateException(result);
        }
        String password = request.getPassword();
        if (!password.equals(request.getPasswordConfirm())) {
            throw new FormValidateException("password", "password confirm not equal password");
        }

        return studentService.createStudent(request);
    }

    @PutMapping("/student")
    public StudentResponse updateStudent(@RequestBody @Valid StudentRequest request, BindingResult result) {
        if (result.hasErrors()) {
            throw new FormValidateException(result);
        }
        return studentService.updateStudent(request);
    }

    @GetMapping("/student")
    public StudentListResponse getStudent() {
        return studentService.getStudent();
    }

    @PostMapping("/student/import")
    public String importStudent(@RequestParam("file") MultipartFile file) throws IOException {
        studentService.importStudent(file);
        return "success";
}
}

