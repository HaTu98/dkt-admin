package vn.edu.vnu.uet.dktadmin.dto.dao.semester;

import vn.edu.vnu.uet.dktadmin.dto.model.Semester;

import java.util.List;

public interface SemesterDao {
    Semester getById(Long id);
    Semester getBySemesterCode(String semesterCode);
    Semester store(Semester semester);
    List<Semester> getAll();
    List<Semester> getLikeCode(String code);
    List<Semester> getLikeName(String name);
}
