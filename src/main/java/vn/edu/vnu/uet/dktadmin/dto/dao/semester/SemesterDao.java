package vn.edu.vnu.uet.dktadmin.dto.dao.semester;

import vn.edu.vnu.uet.dktadmin.dto.model.Semester;

public interface SemesterDao {
    Semester getById(Long id);
    Semester getBySemesterCode(String semesterCode);
    Semester store(Semester semester);
}
