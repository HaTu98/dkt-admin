package vn.edu.vnu.uet.dktadmin.dto.dao.subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.dto.model.Subject;
import vn.edu.vnu.uet.dktadmin.dto.repository.SubjectRepository;

@Service
public class SubjectDaoImpl implements SubjectDao{
    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public Subject getBySubjectCode(String subjectCode) {
        return subjectRepository.findBySubjectCode(subjectCode);
    }
}
