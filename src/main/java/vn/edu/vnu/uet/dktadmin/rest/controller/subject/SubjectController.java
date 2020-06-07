package vn.edu.vnu.uet.dktadmin.rest.controller.subject;

import org.apache.poi.ss.usermodel.CellStyle;
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
import vn.edu.vnu.uet.dktadmin.dto.service.subject.SubjectService;
import vn.edu.vnu.uet.dktadmin.rest.model.ApiDataResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.CheckExistRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.PageBase;
import vn.edu.vnu.uet.dktadmin.rest.model.subject.ListSubjectResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.subject.SubjectRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.subject.SubjectResponse;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequestMapping("/admin")
@RestController
public class SubjectController {
    private static final Logger log = LoggerFactory.getLogger(SubjectController.class);
    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @PostMapping("/subject")
    public ApiDataResponse<SubjectResponse> createSubject(@RequestBody SubjectRequest request) {
        try {
            log.info("create subject {}", request);
            return ApiDataResponse.ok(subjectService.createSubject(request));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @PutMapping("/subject")
    public ApiDataResponse<SubjectResponse> updateSubject(@RequestBody SubjectRequest request) {
        try {
            log.info("update subject {}", request);
            return ApiDataResponse.ok(subjectService.updateSubject(request));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @DeleteMapping("/subject/{id}")
    public ApiDataResponse<String> deleteSubject(@PathVariable Long id) {
        try {
            log.info("delete subject {}", id);
            subjectService.deleteSubject(id);
            return ApiDataResponse.ok("success");
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/subject/all")
    public ApiDataResponse<ListSubjectResponse> getSubject(
            @RequestParam(required = false, value = "Size") Integer size,
            @RequestParam(required = false, value = "Page") Integer page
    ) {
        try {
            log.info("get subject all");
            PageBase pageBase = PageUtil.validate(page, size);
            return ApiDataResponse.ok(subjectService.getSubject(pageBase));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/subject/find")
    public ApiDataResponse<ListSubjectResponse> search(
            @RequestParam(value = "Query") String query,
            @RequestParam(required = false, value = "Size") Integer size,
            @RequestParam(required = false, value = "Page") Integer page
    ) {
        try {
            log.info("get subject search query : {}", query);
            PageBase pageBase = PageUtil.validate(page, size);
            return ApiDataResponse.ok(subjectService.searchSubject(query, pageBase));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/subject_not_in_semester/{id}")
    public ApiDataResponse<ListSubjectResponse> subjectNotInSemester(
            @PathVariable Long id,
            @RequestParam(required = false, value = "Size") Integer size,
            @RequestParam(required = false, value = "Page") Integer page
    ) {
        try {
            log.info("get subject not in semester all");
            PageBase pageBase = PageUtil.validate(page, size);
            return ApiDataResponse.ok(subjectService.getSubjectNotInSemester(id, pageBase));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @PostMapping("/subject/check_exist")
    public ApiDataResponse existSubject (@RequestBody CheckExistRequest checkExistRequest) {
        try {
            log.info("check exist Subject");
            return ApiDataResponse.ok(subjectService.checkExistSubject(checkExistRequest));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @DeleteMapping("/subject/list")
    public ApiDataResponse<String> deleteListSubject(@RequestBody List<Long> id) {
        try {
            log.info("delete subject in list: {}", id);
            subjectService.deleteListSubject(id);
            return ApiDataResponse.ok("success");
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/subject/template")
    public ResponseEntity<?> template(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        XSSFWorkbook xssfWorkbook = subjectService.template();
        String excelFileName = "Template_Subject.xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + excelFileName);
        ServletOutputStream out = response.getOutputStream();
        xssfWorkbook.write(out);
        out.flush();
        out.close();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/subject/import")
    public ResponseEntity<?> importSubject(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException {
        List<XSSFRow> errors = subjectService.importSubject(file);
        if (errors.size() > 0) {
            Workbook fileErrors = subjectService.template();
            CellStyle cellStyle = ExcelUtil.createCenterCellStyle(fileErrors);
            Sheet sheetErrors = fileErrors.getSheetAt(0);
            for (int i = 0 ; i < errors.size(); i++) {
                Row rowOld = errors.get(i);
                Row rowNew = sheetErrors.createRow(4 + i);
                ExcelUtil.copyRow(rowNew, rowOld, cellStyle);
            }
            response.setContentType("application/vnd.ms-excel");
            String excelFileName = "Errors_Subject.xlsx";
            response.setHeader("Content-Disposition", "attachment; filename=" + excelFileName);
            ServletOutputStream out = response.getOutputStream();
            fileErrors.write(out);
            out.flush();
            out.close();
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.ok("success");
    }

    @GetMapping("/subject/export")
    public ResponseEntity<?> export(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        Workbook workbook = subjectService.export();
        String excelFileName = "Subjects.xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + excelFileName);
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
        out.close();
        return ResponseEntity.ok().build();
    }
}
