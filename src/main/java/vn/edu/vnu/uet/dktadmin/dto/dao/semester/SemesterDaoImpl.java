package vn.edu.vnu.uet.dktadmin.dto.dao.semester;

import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.dto.model.Semester;
import vn.edu.vnu.uet.dktadmin.dto.repository.SemesterRepository;

import java.util.List;

@Service
public class SemesterDaoImpl implements SemesterDao{
    private final SemesterRepository semesterRepository;

    public SemesterDaoImpl(SemesterRepository semesterRepository) {
        this.semesterRepository = semesterRepository;
    }

    @Override
    public Semester getById(Long semesterId) {
        return  semesterRepository.findById(semesterId).orElse(null);
    }

    @Override
    public Semester getBySemesterCode(String semesterCode) {
        return semesterRepository.findBySemesterCode(semesterCode);
    }

    @Override
    public Semester store(Semester semester) {
        return semesterRepository.save(semester);
    }

    @Override
    public List<Semester> getAll() {
        return semesterRepository.findAllByOrderByIdAsc();
    }

    @Override
    public List<Semester> getLikeCode(String code) {
        return semesterRepository.findBySemesterCodeContains(code);
    }

    @Override
    public List<Semester> getLikeName(String name) {
        return semesterRepository.findBySemesterNameContains(name);
    }

    @Override
    public List<Semester> getSemesterIdIn(List<Long> ids) {
        return semesterRepository.findByIdIn(ids);
    }

    @Override
    public void deleteSemester(List<Semester> semesters) {
        semesterRepository.deleteAll(semesters);
    }

    @Override
    public Semester getBySemesterName(String semesterName) {
        return semesterRepository.findBySemesterName(semesterName);
    }
}
