package vn.edu.vnu.uet.dktadmin.dto.dao.subjectSemester;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.dto.model.SubjectSemester;
import vn.edu.vnu.uet.dktadmin.dto.repository.SubjectSemesterRepository;

import java.util.Optional;

@Service
public class SubjectSemesterDaoImpl implements SubjectSemesterDao {
    @Autowired
    private SubjectSemesterRepository subjectSemesterRepository;

    @Override
    public SubjectSemester store(SubjectSemester subjectSemester) {
        return subjectSemesterRepository.save(subjectSemester);
    }

    @Override
    public SubjectSemester getById(Long id) {
        Optional<SubjectSemester> subjectSemester = subjectSemesterRepository.findById(id);
        return subjectSemester.orElseGet(subjectSemester::get);
    }
}
