package vn.edu.vnu.uet.dktadmin.rest.controller.semester;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.vnu.uet.dktadmin.dto.service.semester.SemesterService;
import vn.edu.vnu.uet.dktadmin.rest.model.ApiDataResponse;
import vn.edu.vnu.uet.dktadmin.rest.model.semester.SemesterRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.semester.SemesterResponse;

@RestController
@RequestMapping("/admin/semester")
public class SemesterController {

    @Autowired
    private SemesterService semesterService;

    @PostMapping
    public ApiDataResponse<SemesterResponse> create(@RequestBody SemesterRequest request){
        return ApiDataResponse.ok(semesterService.create(request));
    }
}
