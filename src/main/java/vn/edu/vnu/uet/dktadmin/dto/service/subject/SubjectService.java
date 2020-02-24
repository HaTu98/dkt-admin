package vn.edu.vnu.uet.dktadmin.dto.service.subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.dto.dao.subject.SubjectDao;
import vn.edu.vnu.uet.dktadmin.dto.model.Subject;

@Service
public class SubjectService {
    @Autowired
    private SubjectDao subjectDao;

    public boolean existSubject(String subjectCode) {
        Subject subject = subjectDao.getBySubjectCode(subjectCode);
        return subject != null;
    }

}
