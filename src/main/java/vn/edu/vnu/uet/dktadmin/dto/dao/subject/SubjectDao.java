package vn.edu.vnu.uet.dktadmin.dto.dao.subject;

import vn.edu.vnu.uet.dktadmin.dto.model.Subject;

public interface SubjectDao {
    Subject getBySubjectCode(String subjectCode);
    Subject getById(Long id);
}
