package vn.edu.vnu.uet.dktadmin.dto.dao.student;

import vn.edu.vnu.uet.dktadmin.dto.model.Student;

import java.util.List;

public interface StudentDao {
    Student getByEmail(String email);
    Student getByUsername(String username);
    Student save(Student student);
    List<Student> getAll();
}
