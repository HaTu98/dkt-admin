package vn.edu.vnu.uet.dktadmin.dto.dao.subject;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.edu.vnu.uet.dktadmin.dto.model.Subject;
import vn.edu.vnu.uet.dktadmin.dto.repository.SubjectRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubjectDaoImpl implements SubjectDao {
    private final SubjectRepository subjectRepository;

    public SubjectDaoImpl(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

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

    @Override
    public List<Subject> getAll() {
        List<Subject> subjects = subjectRepository.findAll();
        if (CollectionUtils.isEmpty(subjects)) {
            return new ArrayList<>();
        }
        return subjects;
    }

    @Override
    public List<Subject> getLikeName(String name) {
        List<Subject> subjects = subjectRepository.findBySubjectNameContains(name);
        if(CollectionUtils.isEmpty(subjects)) {
            return new ArrayList<>();
        }
        return subjects;
    }

    @Override
    public List<Subject> getLikeCode(String code) {
        List<Subject> subjects = subjectRepository.findBySubjectCodeContains(code);
        if (CollectionUtils.isEmpty(subjects)) {
            return new ArrayList<>();
        }
        return subjects;
    }
}
