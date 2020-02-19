package vn.edu.vnu.uet.dktadmin.dto.dao.semester;

import vn.edu.vnu.uet.dktadmin.dto.model.Semester;

public interface SemesterDao {
    Semester getBySemesterCode(String semesterCode);
}
