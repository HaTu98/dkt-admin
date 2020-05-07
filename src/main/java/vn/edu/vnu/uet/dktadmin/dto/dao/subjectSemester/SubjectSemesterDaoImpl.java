package vn.edu.vnu.uet.dktadmin.dto.dao.subjectSemester;

import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.dto.model.SubjectSemester;
import vn.edu.vnu.uet.dktadmin.dto.repository.SubjectSemesterRepository;

import java.util.List;

@Service
public class SubjectSemesterDaoImpl implements SubjectSemesterDao {
    private final SubjectSemesterRepository subjectSemesterRepository;

    public SubjectSemesterDaoImpl(SubjectSemesterRepository subjectSemesterRepository) {
        this.subjectSemesterRepository = subjectSemesterRepository;
    }

    @Override
    public SubjectSemester store(SubjectSemester subjectSemester) {
        return subjectSemesterRepository.save(subjectSemester);
    }

    @Override
    public SubjectSemester getById(Long id) {
        return subjectSemesterRepository.findById(id).orElse(null);
    }

    @Override
    public SubjectSemester getBySubjectIdAndSemesterId(Long subjectId, Long semesterId) {
        return subjectSemesterRepository.findBySubjectIdAndSemesterId(subjectId, semesterId);
    }

    @Override
    public List<SubjectSemester> getBySemesterId(Long semesterId) {
        return subjectSemesterRepository.findBySemesterId(semesterId);
    }
}
