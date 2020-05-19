package vn.edu.vnu.uet.dktadmin.rest.controller.studentSubject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import vn.edu.vnu.uet.dktadmin.common.exception.BaseException;
import vn.edu.vnu.uet.dktadmin.common.utilities.PageUtil;
import vn.edu.vnu.uet.dktadmin.dto.service.studentSubject.StudentSubjectService;
import vn.edu.vnu.uet.dktadmin.rest.controller.student.StudentController;
import vn.edu.vnu.uet.dktadmin.rest.model.ApiDataResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.PageBase;
import vn.edu.vnu.uet.dktadmin.rest.model.studentSubject.*;

@RestController
@RequestMapping("/admin/student_subject")
public class StudentSubjectController {
    private static final Logger log = LoggerFactory.getLogger(StudentSubjectController.class);
    private final StudentSubjectService studentSubjectService;

    public StudentSubjectController(StudentSubjectService studentSubjectService) {
        this.studentSubjectService = studentSubjectService;
    }

    @PostMapping()
    public ApiDataResponse<StudentSubjectResponse> create(@RequestBody StudentSubjectRequest request) {
        try {
            log.info("create subjectSubject : {}", request );
            StudentSubjectResponse response = studentSubjectService.create(request);
            return ApiDataResponse.ok(response);
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @PutMapping()
    public ApiDataResponse<StudentSubjectResponse> update(@RequestBody StudentSubjectRequest request) {
        try {
            log.info("update subjectSubject : {}", request );
            StudentSubjectResponse response = studentSubjectService.update(request);
            return ApiDataResponse.ok(response);
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/subject_semester/{id}")
    public ApiDataResponse<ListStudentSubjectResponse> getBySubjectSemester(
            @PathVariable Long id,
            @RequestParam(required = false, value = "Size") Integer size,
            @RequestParam(required = false, value = "Page") Integer page
    ) {
        try {
            log.info("get subject_semester id : {}", id );
            PageBase pageBase = PageUtil.validate(page, size);
            ListStudentSubjectResponse response = studentSubjectService.getBySubjectSemesterId(id,pageBase);
            return ApiDataResponse.ok(response);
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/semester/{id}")
    public ApiDataResponse<ListStudentSubjectResponse> getBySemester(
            @PathVariable Long id,
            @RequestParam(required = false, value = "Size") Integer size,
            @RequestParam(required = false, value = "Page") Integer page
    ) {
        try {
            log.info("get by semester id : {}", id );
            PageBase pageBase = PageUtil.validate(page, size);
            ListStudentSubjectResponse response = studentSubjectService.getBySemesterId(id,pageBase);
            return ApiDataResponse.ok(response);
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @DeleteMapping("/{id}")
    public ApiDataResponse<String> delete(@PathVariable Long id) {
        try {
            log.info("delete  id : {}", id );
            studentSubjectService.delete(id);
            return ApiDataResponse.ok("success");
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/{id}")
    public ApiDataResponse<StudentSubjectResponse> getById(@PathVariable Long id) {
        try {
            log.info("get  id : {}", id );
            return ApiDataResponse.ok(studentSubjectService.getById(id));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/semester/{id}/unregistered")
    public ApiDataResponse<StudentSubjectResponse> getStudentSubjectUnregister(
            @PathVariable Long id,
            @RequestParam(required = false, value = "Size") Integer size,
            @RequestParam(required = false, value = "Page") Integer page
    ) {
        try {
            log.info("get student subject unregistered");
            PageBase pageBase = PageUtil.validate(page, size);
            return ApiDataResponse.ok(studentSubjectService.getStudentSubjectUnregistered(id, pageBase));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/student_in_subject/{id}")
    public ApiDataResponse<ListStudentInSubjectResponse> getStudentInSubjectSemester(
            @PathVariable Long id,
            @RequestParam(required = false, value = "Size") Integer size,
            @RequestParam(required = false, value = "Page") Integer page
    ) {
        try {
            log.info("get student subject in subject");
            PageBase pageBase = PageUtil.validate(page,size);
            return ApiDataResponse.ok(studentSubjectService.getListStudentInSubject(id, pageBase));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }
}
