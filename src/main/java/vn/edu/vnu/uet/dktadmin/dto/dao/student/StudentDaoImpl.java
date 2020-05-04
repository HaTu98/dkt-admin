package vn.edu.vnu.uet.dktadmin.dto.dao.student;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.edu.vnu.uet.dktadmin.dto.model.Student;
import vn.edu.vnu.uet.dktadmin.dto.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentDaoImpl implements StudentDao {
    private final StudentRepository studentRepository;

    public StudentDaoImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student getByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    @Override
    public Student getByUsername(String username) {
        return studentRepository.findByUsername(username);
    }

    @Override
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public void delete(Student student) {
        studentRepository.delete(student);
    }

    @Override
    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    @Override
    public void saveAll(List<Student> students) {
        studentRepository.saveAll(students);
    }

    @Override
    public Student getByStudentCode(String studentCode) {
        return studentRepository.findByStudentCode(studentCode);
    }

    @Override
    public Student getById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    @Override
    public List<Student> getStudentInList(List<Long> ids) {
        return studentRepository.findByIdIn(ids);
    }

    @Override
    public List<Student> getStudentLikeName(String name) {
        List<Student> students = studentRepository.findByFullNameContains(name);
        if (CollectionUtils.isEmpty(students)) {
            return new ArrayList<>();
        }
        return students;
    }

    @Override
    public List<Student> getStudentLikeCode(String code) {
        List<Student> students = studentRepository.findByStudentCodeContains(code);
        if (CollectionUtils.isEmpty(students)) {
            return new ArrayList<>();
        }
        return students;
    }

    @Override
    public void deleteListStudent(List<Student> students) {
        studentRepository.deleteAll(students);
    }


}
