package vn.edu.vnu.uet.dktadmin.dto.dao.subject;

import vn.edu.vnu.uet.dktadmin.dto.model.Subject;

import java.util.List;

public interface SubjectDao {
    Subject getBySubjectCode(String subjectCode);
    Subject getById(Long id);
    Subject store(Subject subject);
    void delete(Long id);
    List<Subject> getAll() ;
    List<Subject> getLikeName(String name);
    List<Subject> getLikeCode(String code);
    List<Subject> getByIdIn(List<Long> id);
    void deleteListSubject(List<Subject> subjects);
    List<Subject> getByIdNotIn(List<Long> ids);
}
