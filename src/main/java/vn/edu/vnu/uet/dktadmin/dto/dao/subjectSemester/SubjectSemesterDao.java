package vn.edu.vnu.uet.dktadmin.dto.dao.subjectSemester;

import vn.edu.vnu.uet.dktadmin.dto.model.SubjectSemester;

public interface SubjectSemesterDao {
    SubjectSemester store(SubjectSemester subjectSemester);
    SubjectSemester getById(Long id);
}
