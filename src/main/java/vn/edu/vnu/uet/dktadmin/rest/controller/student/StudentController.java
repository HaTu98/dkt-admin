package vn.edu.vnu.uet.dktadmin.rest.controller.student;

import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnu.uet.dktadmin.common.exception.BaseException;
import vn.edu.vnu.uet.dktadmin.common.security.AccountService;
import vn.edu.vnu.uet.dktadmin.common.utilities.PageUtil;
import vn.edu.vnu.uet.dktadmin.common.validator.EmailValidator;
import vn.edu.vnu.uet.dktadmin.dto.dao.student.StudentDao;
import vn.edu.vnu.uet.dktadmin.dto.service.student.StudentService;
import vn.edu.vnu.uet.dktadmin.rest.controller.BaseController;
import vn.edu.vnu.uet.dktadmin.rest.model.ApiDataResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.PageBaseRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.student.StudentListResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.student.StudentRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.student.StudentResponse;

import java.io.IOException;

@RequestMapping("/admin")
@RestController
public class StudentController extends BaseController {
    private final StudentDao studentDao;
    private final EmailValidator emailValidator;
    private final StudentService studentService;
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;

    public StudentController(StudentDao studentDao, EmailValidator emailValidator, StudentService studentService, PasswordEncoder passwordEncoder, AccountService accountService) {
        this.studentDao = studentDao;
        this.emailValidator = emailValidator;
        this.studentService = studentService;
        this.passwordEncoder = passwordEncoder;
        this.accountService = accountService;
    }

    @PostMapping("/student")
    public ApiDataResponse<StudentResponse> createAccount(@RequestBody StudentRequest request) {
        try {
            return ApiDataResponse.ok(studentService.createStudent(request));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @PutMapping("/student")
    public ApiDataResponse<StudentResponse> updateStudent(@RequestBody StudentRequest request) {
        try {
            return ApiDataResponse.ok(studentService.updateStudent(request));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/student_in_semester/{semesterId}")
    public ApiDataResponse<StudentListResponse> getStudentInSemester(@PathVariable Long semesterId, @RequestParam @Nullable PageBaseRequest pageRequest) {
        try {
            pageRequest = PageUtil.validate(pageRequest);
            return ApiDataResponse.ok(studentService.getStudentInSemester(semesterId, pageRequest));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/student_in_subject/{subjectSemesterId}")
    public ApiDataResponse<StudentResponse> getStudentInSubject(@PathVariable Long subjectSemesterId, @RequestParam @Nullable PageBaseRequest pageRequest) {
        try {
            pageRequest = PageUtil.validate(pageRequest);
            return ApiDataResponse.ok(studentService.getStudentInSubject(subjectSemesterId, pageRequest));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/student")
    public ApiDataResponse<StudentListResponse> getAllStudent() {
        try {
            return ApiDataResponse.ok(studentService.getAllStudent());
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/student/{id}")
    public ApiDataResponse<StudentResponse> getStudent(@PathVariable Long id) {
        try {
            return ApiDataResponse.ok(studentService.getStudent(id));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @DeleteMapping("/student/{id}")
    public ApiDataResponse<StudentResponse> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return ApiDataResponse.ok("success");
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/student/find")
    public ApiDataResponse<StudentResponse> findStudent(@RequestParam(value = "Query") String query, @RequestParam @Nullable PageBaseRequest pageBaseRequest) {
        try {
            pageBaseRequest = PageUtil.validate(pageBaseRequest);
            return ApiDataResponse.ok(studentService.searchStudent(query, pageBaseRequest));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @PostMapping("/student/import")
    public ApiDataResponse<String> importStudent(@RequestParam("file") MultipartFile file) throws IOException {
        studentService.importStudent(file);
        return ApiDataResponse.ok("success");
    }
}

