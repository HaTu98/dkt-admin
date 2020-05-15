package vn.edu.vnu.uet.dktadmin.rest.controller.student;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import vn.edu.vnu.uet.dktadmin.rest.model.PageBase;
import vn.edu.vnu.uet.dktadmin.rest.model.student.StudentListResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.student.StudentRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.student.StudentResponse;

import java.io.IOException;
import java.util.List;

@RequestMapping("/admin")
@RestController
public class StudentController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(StudentController.class);
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
            log.info("create student {}", request);
            return ApiDataResponse.ok(studentService.createStudent(request));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @PutMapping("/student")
    public ApiDataResponse<StudentResponse> updateStudent(@RequestBody StudentRequest request) {
        try {
            log.info("update student {}", request);
            return ApiDataResponse.ok(studentService.updateStudent(request));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/student_in_semester/{semesterId}")
    public ApiDataResponse<StudentListResponse> getStudentInSemester(
            @PathVariable Long semesterId,
            @RequestParam(required = false, value = "Size") Integer size,
            @RequestParam(required = false, value = "Page") Integer page
    )
    {
        try {
            PageBase pageRequest = PageUtil.validate(page, size);
            log.info("get student in semester {}", semesterId);
            return ApiDataResponse.ok(studentService.getStudentInSemester(semesterId, pageRequest));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/student_in_subject/{subjectSemesterId}")
    public ApiDataResponse<StudentResponse> getStudentInSubject(
            @PathVariable Long subjectSemesterId,
            @RequestParam(required = false, value = "Size") Integer size,
            @RequestParam(required = false, value = "Page") Integer page
    )
    {
        try {
            log.info("get student in subject {}", subjectSemesterId);
            PageBase pageBase = PageUtil.validate(page, size);
            return ApiDataResponse.ok(studentService.getStudentInSubject(subjectSemesterId, pageBase));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/student/all")
    public ApiDataResponse<StudentListResponse> getAllStudent(
            @RequestParam(required = false, value = "Size") Integer size,
            @RequestParam(required = false, value = "Page") Integer page
    ) {
        try {
            log.info("get all student}");
            PageBase pageBase = PageUtil.validate(page, size);
            return ApiDataResponse.ok(studentService.getAllStudent(pageBase));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/student/{id}")
    public ApiDataResponse<StudentResponse> getStudent(@PathVariable Long id) {
        try {
            log.info("get student by id :  {}", id);
            return ApiDataResponse.ok(studentService.getStudent(id));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @DeleteMapping("/student/{id}")
    public ApiDataResponse<StudentResponse> deleteStudent(@PathVariable Long id) {
        try {
            log.info("delete student id : {}", id);
            studentService.deleteStudent(id);
            return ApiDataResponse.ok("success");
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @DeleteMapping("/student/list")
    public ApiDataResponse<String> deleteListStudent(@RequestBody List<Long> id) {
        try {
            log.info("delete student in list: {}", id);
            studentService.deleteListStudent(id);
            return ApiDataResponse.ok("success");
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/student/find")
    public ApiDataResponse<StudentResponse> findStudent(
            @RequestParam(value = "Query") String query,
            @RequestParam(required = false, value = "Size") Integer size,
            @RequestParam(required = false, value = "Page") Integer page
    ) {
        try {
            log.info("find student query : {}", query);
            PageBase pageBase = PageUtil.validate(page, size);
            return ApiDataResponse.ok(studentService.searchStudent(query, pageBase));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @PostMapping("/student/import")
    public ApiDataResponse<String> importStudent(@RequestParam("file") MultipartFile file) throws IOException {
        studentService.importStudent(file);
        return ApiDataResponse.ok("success");
    }
}

