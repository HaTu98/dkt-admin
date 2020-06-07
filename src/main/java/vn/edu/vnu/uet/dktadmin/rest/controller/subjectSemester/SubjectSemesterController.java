package vn.edu.vnu.uet.dktadmin.    rest.controller.subjectSemester;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import vn.edu.vnu.uet.dktadmin.common.exception.BaseException;
import vn.edu.vnu.uet.dktadmin.common.utilities.PageUtil;
import vn.edu.vnu.uet.dktadmin.dto.service.subjectSemester.SubjectSemesterService;
import vn.edu.vnu.uet.dktadmin.rest.controller.subject.SubjectController;
import vn.edu.vnu.uet.dktadmin.rest.model.ApiDataResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.PageBase;
import vn.edu.vnu.uet.dktadmin.rest.model.subjectSemester.SubjectConflictResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.subjectSemester.SubjectSemesterRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.subjectSemester.SubjectSemesterResponse;

import java.util.List;

@RestController
@RequestMapping("/admin/subject_semesters")
public class SubjectSemesterController {
    private static final Logger log = LoggerFactory.getLogger(SubjectController.class);
    private final SubjectSemesterService subjectSemesterService;

    public SubjectSemesterController(SubjectSemesterService subjectSemesterService) {
        this.subjectSemesterService = subjectSemesterService;
    }

    @PostMapping
    public ApiDataResponse<SubjectSemesterResponse> create(@RequestBody SubjectSemesterRequest request) {
        try {
            log.info("create subject_semesters semester : {}", request);
            return ApiDataResponse.ok(subjectSemesterService.create(request));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @PostMapping("/list")
    public ApiDataResponse<String> createList(@RequestBody List<SubjectSemesterRequest> requests) {
        log.info("create list subjectSemester : {}", requests);
        for ( SubjectSemesterRequest request : requests) {
            try {
                subjectSemesterService.create(request);
            } catch (BaseException e) {
                log.error(e.getMessage());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return ApiDataResponse.ok("success");
    }

    @DeleteMapping("/list")
    public ApiDataResponse<String> deleteList(@RequestBody List<Long> ids) {
        try {
            log.info("delete subjectSemester in list: {}", ids);
            subjectSemesterService.deleteListSubjectSemester(ids);
            return ApiDataResponse.ok("success");
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @PutMapping
    public ApiDataResponse<SubjectSemesterResponse> update(@RequestBody SubjectSemesterRequest request) {
        try {
            log.info("Update subject_semesters semester : {}", request);
            return ApiDataResponse.ok(subjectSemesterService.update(request));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/{id}")
    public ApiDataResponse<SubjectSemesterResponse> getById(@PathVariable Long id) {
        try {
            log.info("get subject_semesters semester  id : {}", id);
            return ApiDataResponse.ok(subjectSemesterService.getById(id));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/semester/{id}")
    public ApiDataResponse<SubjectSemesterResponse> getAll(
            @PathVariable(required = false, value = "id") Long id,
            @RequestParam(required = false, value = "Size") Integer size,
            @RequestParam(required = false, value = "Page") Integer page
    )
    {
        try {
            log.info("get semester by  semester : {}", id);
            PageBase pageBase = PageUtil.validate(page, size);
            return ApiDataResponse.ok(subjectSemesterService.getSemester(id,pageBase));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/subject_conflict/semester/{id}")
    public ApiDataResponse<List<SubjectConflictResponse>> getConflictSubject(@PathVariable Long id){
        try {
            log.info("get subject conflict by  semester : {}", id);
            return ApiDataResponse.ok(subjectSemesterService.getSubjectConflict(id));
        } catch (BaseException e) {
            log.error(e.getMessage());
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiDataResponse.error();
        }
    }

}
