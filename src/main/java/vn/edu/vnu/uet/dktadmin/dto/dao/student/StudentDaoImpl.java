package vn.edu.vnu.uet.dktadmin.dto.dao.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.dto.model.Student;
import vn.edu.vnu.uet.dktadmin.dto.repository.StudentRepository;

import java.util.List;

@Service
public class StudentDaoImpl implements StudentDao{
    @Autowired
    private StudentRepository studentRepository;

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
    public List<Student> getAll() {
        return studentRepository.findAll();
    }
}
