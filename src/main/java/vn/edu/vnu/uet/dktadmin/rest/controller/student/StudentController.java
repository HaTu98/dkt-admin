package vn.edu.vnu.uet.dktadmin.rest.controller.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnu.uet.dktadmin.common.exception.FormValidateException;
import vn.edu.vnu.uet.dktadmin.common.security.AccountService;
import vn.edu.vnu.uet.dktadmin.common.validator.EmailValidator;
import vn.edu.vnu.uet.dktadmin.dto.dao.student.StudentDao;
import vn.edu.vnu.uet.dktadmin.dto.service.student.StudentService;
import vn.edu.vnu.uet.dktadmin.rest.controller.BaseController;
import vn.edu.vnu.uet.dktadmin.rest.model.ApiDataResponse;
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
    public ApiDataResponse<StudentResponse> createAccount(@RequestBody StudentRequest request) {
        String password = request.getPassword();
        if (!password.equals(request.getPasswordConfirm())) {
            return ApiDataResponse.error(HttpStatus.BAD_REQUEST.value(), "password confirm not equal password");
        }

        return ApiDataResponse.ok(studentService.createStudent(request));
    }

    @PutMapping("/student")
    public ApiDataResponse<StudentResponse> updateStudent(@RequestBody StudentRequest request) {
        return ApiDataResponse.ok(studentService.updateStudent(request));
    }

    @GetMapping("/student")
    public ApiDataResponse<StudentListResponse> getStudent() {
        return ApiDataResponse.ok(studentService.getStudent());
    }

    @PostMapping("/student/import")
    public ApiDataResponse<String> importStudent(@RequestParam("file") MultipartFile file) throws IOException {
        studentService.importStudent(file);
        return ApiDataResponse.ok("success");
    }
}

