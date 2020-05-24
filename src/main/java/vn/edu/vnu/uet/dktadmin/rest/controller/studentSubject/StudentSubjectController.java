package vn.edu.vnu.uet.dktadmin.rest.controller.studentSubject;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.vnu.uet.dktadmin.common.exception.BaseException;
import vn.edu.vnu.uet.dktadmin.common.utilities.ExcelUtil;
import vn.edu.vnu.uet.dktadmin.common.utilities.PageUtil;
import vn.edu.vnu.uet.dktadmin.dto.service.studentSubject.StudentSubjectService;
import vn.edu.vnu.uet.dktadmin.rest.controller.student.StudentController;
import vn.edu.vnu.uet.dktadmin.rest.model.ApiDataResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.PageBase;
import vn.edu.vnu.uet.dktadmin.rest.model.studentSubject.*;
import vn.edu.vnu.uet.dktadmin.rest.model.subjectSemester.SubjectSemesterRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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
            log.info("create subjectSemester : {}", request);
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
            log.info("update subjectSubject : {}", request);
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

    @PutMapping("/status")
    public ApiDataResponse<String> active(@RequestBody StudentSubjectStatus request) {
        try {
            log.info("update status : {}", request);
            studentSubjectService.updateStatus(request);
            return ApiDataResponse.ok("success");
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
            log.info("get subject_semester id : {}", id);
            PageBase pageBase = PageUtil.validate(page, size);
            ListStudentSubjectResponse response = studentSubjectService.getBySubjectSemesterId(id, pageBase);
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
            log.info("get by semester id : {}", id);
            PageBase pageBase = PageUtil.validate(page, size);
            ListStudentSubjectResponse response = studentSubjectService.getBySemesterId(id, pageBase);
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
            log.info("delete  id : {}", id);
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

    @DeleteMapping("/list")
    public ApiDataResponse<String> deleteList(@RequestBody List<Long> ids) {
        try {
            log.info("delete  ids : {}", ids);
            studentSubjectService.deleteList(ids);
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
            log.info("get  id : {}", id);
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
            PageBase pageBase = PageUtil.validate(page, size);
            return ApiDataResponse.ok(studentSubjectService.getListStudentInSubject(id, pageBase));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @PostMapping("/add_one")
    public ApiDataResponse<StudentSubjectResponse> createOne(@RequestBody StudentSubjectRequest request) {
        try {
            log.info("create  student in StudentSubject");
            return ApiDataResponse.ok(studentSubjectService.createByStudentCode(request));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @PostMapping("/check_create")
    public ApiDataResponse checkCreate(@RequestBody StudentSubjectRequest request) {
        try {
            log.info("check create");
            studentSubjectService.validateCreateOne(request);
            return ApiDataResponse.ok(null);
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @PostMapping("/import")
    public ResponseEntity<?> importStudent(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        List<XSSFRow> errors = studentSubjectService.importStudentSubject(file);
        if (errors.size() > 0) {
            Workbook fileErrors = studentSubjectService.template();
            Sheet sheetErrors = fileErrors.getSheetAt(0);
            for (int i = 0 ; i < errors.size(); i++) {
                Row rowOld = errors.get(i);
                Row rowNew = sheetErrors.createRow(5 + i);
                ExcelUtil.copyRow(rowNew, rowOld);
            }
            response.setContentType("application/vnd.ms-excel");
            String excelFileName = "Errors_Student_Subject.xlsx";
            response.setHeader("Content-Disposition", "attachment; filename=" + excelFileName);
            ServletOutputStream out = response.getOutputStream();
            fileErrors.write(out);
            out.flush();
            out.close();
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.ok("success");
    }

    @GetMapping("/template")
    public ResponseEntity<?> template(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        XSSFWorkbook xssfWorkbook = studentSubjectService.template();
        String excelFileName = "Template_Student_Subject.xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + excelFileName);
        ServletOutputStream out = response.getOutputStream();
        xssfWorkbook.write(out);
        out.flush();
        out.close();
        return ResponseEntity.ok().build();
    }
}
