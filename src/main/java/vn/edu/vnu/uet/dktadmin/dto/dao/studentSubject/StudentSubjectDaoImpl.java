package vn.edu.vnu.uet.dktadmin.dto.dao.studentSubject;

import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dktadmin.dto.model.StudentSubject;
import vn.edu.vnu.uet.dktadmin.dto.repository.StudentSubjectRepository;

import java.util.List;

@Service
public class StudentSubjectDaoImpl implements StudentSubjectDao {
    private final StudentSubjectRepository studentSubjectRepository;

    public StudentSubjectDaoImpl(StudentSubjectRepository studentSubjectRepository) {
        this.studentSubjectRepository = studentSubjectRepository;
    }

    @Override
    public List<StudentSubject> getAll() {
        return studentSubjectRepository.findAll();
    }

    @Override
    public StudentSubject store(StudentSubject studentSubject) {
        return studentSubjectRepository.save(studentSubject);
    }

    @Override
    public StudentSubject getByStudentAndSubjectSemesterId(Long studentId, Long subjectSemesterId) {
        return studentSubjectRepository.findByStudentIdAndSubjectSemesterId(studentId, subjectSemesterId);
    }

    @Override
    public StudentSubject getById(Long id) {
        return studentSubjectRepository.findById(id).orElse(null);
    }

    @Override
    public List<StudentSubject> getBySemesterId(Long semesterId) {
        return studentSubjectRepository.findBySemesterId(semesterId);
    }

    @Override
    public List<StudentSubject> getBySubjectSemesterId(Long subjectSemesterId) {
        return studentSubjectRepository.findBySubjectSemesterId(subjectSemesterId);
    }

}
