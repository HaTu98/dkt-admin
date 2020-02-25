package vn.edu.vnu.uet.dktadmin.rest.controller.semester;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.vnu.uet.dktadmin.common.exception.FormValidateException;
import vn.edu.vnu.uet.dktadmin.dto.service.semester.SemesterService;
import vn.edu.vnu.uet.dktadmin.rest.model.semester.SemesterRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.semester.SemesterResponse;

@RestController
@RequestMapping("/admin/semester")
public class SemesterController {

    @Autowired
    private SemesterService semesterService;

    @PostMapping
    public SemesterResponse create(@RequestBody SemesterRequest request, BindingResult result){
        if (result.hasErrors()) {
            if (result.hasErrors()) {
                throw new FormValidateException(result);
            }
        }
        return semesterService.create(request);
    }
}
