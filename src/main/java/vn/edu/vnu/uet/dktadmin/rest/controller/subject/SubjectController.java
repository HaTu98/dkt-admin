package vn.edu.vnu.uet.dktadmin.rest.controller.subject;

import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import vn.edu.vnu.uet.dktadmin.common.exception.BaseException;
import vn.edu.vnu.uet.dktadmin.common.utilities.PageUtil;
import vn.edu.vnu.uet.dktadmin.dto.service.subject.SubjectService;
import vn.edu.vnu.uet.dktadmin.rest.model.ApiDataResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.PageBaseRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.subject.ListSubjectResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.subject.SubjectRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.subject.SubjectResponse;

@RequestMapping("/admin")
@RestController
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @PostMapping("/subject")
    public ApiDataResponse<SubjectResponse> createSubject(@RequestBody SubjectRequest request) {
        try {
            return ApiDataResponse.ok(subjectService.createSubject(request));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @PutMapping("/subject")
    public ApiDataResponse<SubjectResponse> updateSubject(@RequestBody SubjectRequest request) {
        try {
            return ApiDataResponse.ok(subjectService.updateSubject(request));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @DeleteMapping("/subject/{id}")
    public ApiDataResponse<String> deleteSubject(@PathVariable Long id) {
        try {
            subjectService.deleteSubject(id);
            return ApiDataResponse.ok("success");
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/subject")
    public ApiDataResponse<ListSubjectResponse> getSubject(@RequestParam @Nullable PageBaseRequest pageRequest) {
        try {
            pageRequest = PageUtil.validate(pageRequest);
            return ApiDataResponse.ok(subjectService.getSubject(pageRequest));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }

    @GetMapping("/subject/find")
    public ApiDataResponse<ListSubjectResponse> search(@RequestParam(value = "Query") String query, @RequestParam @Nullable PageBaseRequest pageRequest) {
        try {
            pageRequest = PageUtil.validate(pageRequest);
            return ApiDataResponse.ok(subjectService.searchSubject(query, pageRequest));
        } catch (BaseException e) {
            return ApiDataResponse.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ApiDataResponse.error();
        }
    }
}
