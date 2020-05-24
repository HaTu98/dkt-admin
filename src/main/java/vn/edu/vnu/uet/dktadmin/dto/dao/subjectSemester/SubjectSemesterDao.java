package vn.edu.vnu.uet.dktadmin.dto.dao.subjectSemester;

import vn.edu.vnu.uet.dktadmin.dto.model.Subject;
import vn.edu.vnu.uet.dktadmin.dto.model.SubjectSemester;

import java.util.List;

public interface SubjectSemesterDao {
    SubjectSemester store(SubjectSemester subjectSemester);
    SubjectSemester getById(Long id);
    SubjectSemester getBySubjectIdAndSemesterId(Long subjectId, Long semesterId);
    List<SubjectSemester> getBySemesterId(Long semesterId);
    List<SubjectSemester> getBySubjectSemesterIdInList(List<Long> ids);
    void deleteList(List<SubjectSemester> subjectSemesters);
    List<SubjectSemester> getBySubjectIdIn(List<Long> ids);
    List<SubjectSemester> getBySemesterIdIn(List<Long> ids);
}
