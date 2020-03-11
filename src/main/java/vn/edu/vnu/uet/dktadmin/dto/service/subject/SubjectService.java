package vn.edu.vnu.uet.dktadmin.dto.service.subject;

import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.dto.dao.subject.SubjectDao;
import vn.edu.vnu.uet.dktadmin.dto.model.Subject;
import vn.edu.vnu.uet.dktadmin.rest.model.subject.SubjectRequest;
import vn.edu.vnu.uet.dktadmin.rest.model.subject.SubjectResponse;

@Service
public class SubjectService {
    @Autowired
    private SubjectDao subjectDao;

    @Autowired
    private MapperFacade mapperFacade;

    public boolean existSubject(String subjectCode) {
        Subject subject = subjectDao.getBySubjectCode(subjectCode);
        return subject != null;
    }

    public SubjectResponse createSubject(SubjectRequest request) {
        Subject subject = mapperFacade.map(request, Subject.class);
         return mapperFacade.map(subjectDao.store(subject),SubjectResponse.class);
    }

}
