package vn.edu.vnu.uet.dktadmin.dto.dao.subjectSemester;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.dto.model.SubjectSemester;
import vn.edu.vnu.uet.dktadmin.dto.repository.SubjectSemesterRepository;

import java.util.Optional;

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
}
