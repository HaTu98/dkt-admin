package vn.edu.vnu.uet.dktadmin.dto.dao.semester;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.dto.model.Semester;
import vn.edu.vnu.uet.dktadmin.dto.repository.SemesterRepository;

import java.util.Optional;

@Service
public class SemesterDaoImpl implements SemesterDao{
    @Autowired
    private SemesterRepository semesterRepository;

    @Override
    public Semester getById(Long semesterId) {
        Optional<Semester> semester = semesterRepository.findById(semesterId);
        return semester.orElseGet(semester::get);
    }

    @Override
    public Semester getBySemesterCode(String semesterCode) {
        return semesterRepository.findBySemesterCode(semesterCode);
    }

    @Override
    public Semester store(Semester semester) {
        return semesterRepository.save(semester);
    }
}
