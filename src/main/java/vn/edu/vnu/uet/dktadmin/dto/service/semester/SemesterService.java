package vn.edu.vnu.uet.dktadmin.dto.service.semester;

import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.common.exception.BadRequestException;
import vn.edu.vnu.uet.dktadmin.common.exception.FormValidateException;
import vn.edu.vnu.uet.dktadmin.dto.dao.semester.SemesterDao;
import vn.edu.vnu.uet.dktadmin.dto.model.Semester;
import vn.edu.vnu.uet.dktadmin.rest.model.semester.SemesterRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.semester.SemesterResponse;

@Service
public class SemesterService {
    @Autowired
    private SemesterDao semesterDao;

    @Autowired
    private MapperFacade mapperFacade;

    public boolean existSemester(Long semesterId){
        Semester semester = semesterDao.getById(semesterId);
        return semester != null;
    }

    public SemesterResponse create(SemesterRequest semesterRequest){
        Semester semester = semesterDao.getById(semesterRequest.getSemesterId());
        if (semester != null)
            throw new BadRequestException(HttpStatus.BAD_REQUEST.value(), "semester already existed");

        semester = mapperFacade.map(semesterRequest, Semester.class);
        Semester semesterSave = semesterDao.store(semester);
        return mapperFacade.map(semesterSave, SemesterResponse.class);
    }
}
