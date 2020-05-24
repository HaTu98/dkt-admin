package vn.edu.vnu.uet.dktadmin.dto.dao.subjectSemester;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.edu.vnu.uet.dktadmin.dto.model.SubjectSemester;
import vn.edu.vnu.uet.dktadmin.dto.repository.SubjectSemesterRepository;

import java.util.ArrayList;
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

    @Override
    public List<SubjectSemester> getBySubjectSemesterIdInList(List<Long> ids) {
        List<SubjectSemester> subjectSemesters = subjectSemesterRepository.findByIdIn(ids);
        if (CollectionUtils.isEmpty(subjectSemesters)) {
            return new ArrayList<>();
        }
        return subjectSemesters;
    }

    @Override
    public void deleteList(List<SubjectSemester> subjectSemesters) {
        subjectSemesterRepository.deleteAll(subjectSemesters);
    }

    @Override
    public List<SubjectSemester> getBySubjectIdIn(List<Long> ids) {
        List<SubjectSemester> subjectSemesters = subjectSemesterRepository.findBySubjectIdIn(ids);
        if (CollectionUtils.isEmpty(subjectSemesters)) {
            return new ArrayList<>();
        }
        return subjectSemesters;
    }

    @Override
    public List<SubjectSemester> getBySemesterIdIn(List<Long> ids) {
        List<SubjectSemester> subjectSemesters = subjectSemesterRepository.findBySemesterIdIn(ids);
        if (CollectionUtils.isEmpty(subjectSemesters)) {
            return new ArrayList<>();
        }
        return subjectSemesters;
    }
}
