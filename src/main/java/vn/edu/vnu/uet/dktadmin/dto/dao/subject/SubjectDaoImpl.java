package vn.edu.vnu.uet.dktadmin.dto.dao.subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.dto.model.Subject;
import vn.edu.vnu.uet.dktadmin.dto.repository.SubjectRepository;

import java.util.Optional;

@Service
public class SubjectDaoImpl implements SubjectDao{
    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public Subject getBySubjectCode(String subjectCode) {
        return subjectRepository.findBySubjectCode(subjectCode);
    }

    @Override
    public Subject getById(Long id) {
        return subjectRepository.findById(id).orElse(null);
    }

    @Override
    public Subject store(Subject subject) {
        return subjectRepository.save(subject);
    }
}
