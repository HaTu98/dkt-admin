package vn.edu.vnu.uet.dktadmin.dto.dao.student;

        import vn.edu.vnu.uet.dktadmin.dto.model.Student;

        import java.util.List;

public interface StudentDao {
    Student getByEmail(String email);
    Student getByUsername(String username);
    Student save(Student student);
    void delete(Student student);
    List<Student> getAll();
    void saveAll(List<Student> students);
    Student getByStudentCode(String studentCode);
    Student getById(Long id);
    List<Student> getStudentInList(List<Long> ids);
}
