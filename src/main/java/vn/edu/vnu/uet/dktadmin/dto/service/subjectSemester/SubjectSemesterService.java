package vn.edu.vnu.uet.dktadmin.dto.service.subjectSemester;

import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.dto.dao.subjectSemester.SubjectSemesterDao;
import vn.edu.vnu.uet.dktadmin.dto.model.SubjectSemester;
import vn.edu.vnu.uet.dktadmin.rest.model.subjectSemester.SubjectSemesterRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.subjectSemester.SubjectSemesterResponse;

@Service
public class SubjectSemesterService {
    @Autowired
    private SubjectSemesterDao subjectSemesterDao;

    @Autowired
    private MapperFacade mapperFacade;

    public SubjectSemesterResponse create(SubjectSemesterRequest request) {
        SubjectSemester subjectSemester = mapperFacade.map(request, SubjectSemester.class);

        return mapperFacade.map(subjectSemesterDao.store(subjectSemester), SubjectSemesterResponse.class);
    }
}
