package vn.edu.vnu.uet.dktadmin.dto.dao.student;

import vn.edu.vnu.uet.dktadmin.dto.model.Student;

public interface StudentDao {
    Student getByEmail(String email);
    Student getByUsername(String username);
    Student save(Student student);
}
